package com.eigenmusik.sources.googledrive;

import com.eigenmusik.exceptions.SourceAuthenticationException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.sources.*;
import com.eigenmusik.tracks.Track;
import com.eigenmusik.tracks.TrackSource;
import com.eigenmusik.tracks.TrackStreamUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleDrive extends Source {

    private static final JacksonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static Logger log = Logger.getLogger(GoogleDrive.class);
    private final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/drive",
            "email",
            "profile");
    private final GoogleDriveUserRepository googleDriveUserRepository;
    private GoogleDriveConfiguration googleDriveConfiguration;
    private GoogleDriveAccessTokenRepository googleDriveAccessTokenRepository;

    @Autowired
    public GoogleDrive(
            SourceAccountRepository sourceAccountRepository,
            GoogleDriveAccessTokenRepository googleDriveAccessTokenRepository,
            GoogleDriveConfiguration googleDriveConfiguration,
            GoogleDriveUserRepository googleDriveUserRepository
    ) {
        super(sourceAccountRepository);
        this.googleDriveAccessTokenRepository = googleDriveAccessTokenRepository;
        this.googleDriveConfiguration = googleDriveConfiguration;
        this.googleDriveUserRepository = googleDriveUserRepository;
    }

    static Userinfoplus getUserInfo(Credential credentials)
            throws UserDoesntExistException {
        Oauth2 userInfoService = new Oauth2.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName("EigenMusik Dev")
                .build();
        Userinfoplus userInfo = null;
        try {
            userInfo = userInfoService.userinfo().get().execute();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        } else {
            throw new UserDoesntExistException();
        }
    }

    /**
     * Build a Drive service object.
     *
     * @param credentials OAuth 2.0 credentials.
     * @return Drive service object.
     */
    static Drive buildService(Credential credentials) {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName("EigenMusik Dev")
                .build();
    }

    private static Boolean isPlayableFormat(String format) {
        if (format == null) {
            return false;
        }
        if (format.equals("wav") || format.equals("mp3")) {
            return true;
        }
        return false;
    }

    @Override
    public TrackStreamUrl getStreamUrl(Track track) {

        GoogleDriveUser googleDriveUser = googleDriveUserRepository.findOne(track.getTrackSource().getOwner().getUri());

        GoogleDriveAccessToken accessToken = googleDriveUser.getAccessToken();
        TokenResponse tokenResponse = new TokenResponse().setAccessToken(accessToken.getAccessToken());
        tokenResponse.setExpiresInSeconds(Long.valueOf(accessToken.getExpiresIn()));
        Credential credentials = new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(
                tokenResponse);
        Drive drive = buildService(credentials);

        try {
            return new TrackStreamUrl(drive.files().get(track.getTrackSource().getUri()).execute().getDownloadUrl() + "&oauth_token=" + accessToken.getAccessToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public SourceAccount getAccount(SourceAccountAuthentication auth) throws SourceAuthenticationException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(auth.getCode())
                    .setRedirectUri(googleDriveConfiguration.getRedirectUrl())
                    .execute();
            Credential credential = flow.createAndStoreCredential(response, null);
            GoogleDriveAccessToken googleDriveAccessToken = new GoogleDriveAccessToken(credential);

            GoogleDriveUser googleDriveUser = new GoogleDriveUser(getUserInfo(credential));
            googleDriveUser.setAccessToken(googleDriveAccessToken);

            googleDriveAccessTokenRepository.save(googleDriveAccessToken);
            googleDriveUserRepository.save(googleDriveUser);

            SourceAccount account = new SourceAccount();
            account.setUri(googleDriveUser.getId());
            account.setSource(SourceType.GOOGLEDRIVE);

            return account;

        } catch (IOException | UserDoesntExistException e) {
            throw new SourceAuthenticationException(SourceType.GOOGLEDRIVE);
        }
    }

    @Override
    public List<Track> getTracks(SourceAccount account) {
        GoogleDriveUser googleDriveUser = googleDriveUserRepository.findOne(account.getUri());

        GoogleDriveAccessToken accessToken = googleDriveUser.getAccessToken();
        TokenResponse tokenResponse = new TokenResponse().setAccessToken(accessToken.getAccessToken());
        tokenResponse.setExpiresInSeconds(Long.valueOf(accessToken.getExpiresIn()));
        Credential credentials = new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(
                tokenResponse);
        Drive drive = buildService(credentials);

        try {
            return drive.files().list().setQ("mimeType contains 'audio'").execute().getItems().stream().map(file -> mapToTrack(file, googleDriveUser, account)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return "Google Drive";
    }

    @Override
    public String getAuthUrl() {
        String scopes = "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.apps.readonly https://www.googleapis.com/auth/drive.file email profile";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("scope", scopes));
        params.add(new BasicNameValuePair("client_id", googleDriveConfiguration.getClientId()));
        params.add(new BasicNameValuePair("response_type", "code"));
        params.add(new BasicNameValuePair("redirect_uri", googleDriveConfiguration.getRedirectUrl()));
        String query = URLEncodedUtils.format(params, "UTF-8");
        return "https://accounts.google.com/o/oauth2/v2/auth?" + query;
    }

    public SourceType getType() {
        return SourceType.GOOGLEDRIVE;
    }

    private Track mapToTrack(File file, GoogleDriveUser user, SourceAccount sourceAccount) {
        TrackSource trackSource = new TrackSource();
        trackSource.setUri(file.getId());
        trackSource.setSource(SourceType.GOOGLEDRIVE);
        trackSource.setOwner(sourceAccount);

        Track track = new Track();
        track.setName(file.getTitle());
        track.setArtist("Drive File");
        track.setTrackSource(trackSource);

        return track;
    }

    private GoogleAuthorizationCodeFlow getFlow() throws IOException {

        JsonFactory jsonFactory = new JacksonFactory();
        NetHttpTransport transport = null;
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, googleDriveConfiguration.getClientId(),
                googleDriveConfiguration.getClientSecret(), SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

}
