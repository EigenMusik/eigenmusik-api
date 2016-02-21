package com.eigenmusik.api.sources.googledrive;

import com.eigenmusik.api.config.EigenMusikConfiguration;
import com.eigenmusik.api.sources.*;
import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.tracks.TrackSource;
import com.eigenmusik.api.tracks.TrackStreamUrl;
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
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Google Drive implementation of the Source abstract.
 */
@Service
public class GoogleDrive extends Source {

    private static final JacksonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static Logger log = Logger.getLogger(GoogleDrive.class);
    private final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.apps.readonly",
            "https://www.googleapis.com/auth/drive.file email profile"
    );
    private final GoogleDriveUserRepository googleDriveUserRepository;
    private final GoogleDriveConfiguration googleDriveConfiguration;
    private final GoogleDriveAccessTokenRepository googleDriveAccessTokenRepository;

    @Autowired
    public GoogleDrive(
            GoogleDriveAccessTokenRepository googleDriveAccessTokenRepository,
            GoogleDriveConfiguration googleDriveConfiguration,
            GoogleDriveUserRepository googleDriveUserRepository
    ) {
        this.googleDriveAccessTokenRepository = googleDriveAccessTokenRepository;
        this.googleDriveConfiguration = googleDriveConfiguration;
        this.googleDriveUserRepository = googleDriveUserRepository;
    }

    /**
     * Static helper method to get user info from a set of credentials
     *
     * @param credentials
     * @return
     * @throws GoogleDriveUserDoesntExistException
     */
    static Userinfoplus getUserInfo(Credential credentials)
            throws GoogleDriveUserDoesntExistException {
        Oauth2 userInfoService = new Oauth2.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(EigenMusikConfiguration.APP_NAME)
                .build();
        Userinfoplus userInfo = null;

        // Try and retrieve the user details from Google.
        try {
            userInfo = userInfoService.userinfo().get().execute();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // Throw an exception if the user is unable.
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        } else {
            throw new GoogleDriveUserDoesntExistException();
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
                .setApplicationName(EigenMusikConfiguration.APP_NAME)
                .build();
    }

    /**
     * Static helper function to map a Google Drive file to an EigenMusik entity.
     *
     * @param file
     * @param user
     * @param sourceAccount
     * @return Track
     */
    private static Track mapToTrack(File file, GoogleDriveUser user, SourceAccount sourceAccount) {
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
            log.error(e.getMessage());
        }
        // TODO should probably return an exception here.
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

        } catch (IOException | GoogleDriveUserDoesntExistException e) {
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
        // Map scope list to a space separated string.
        String scopes = SCOPES.stream().reduce("", (acc, scope) -> acc + " " + scope);
        String query = URLEncodedUtils.format(
                Arrays.asList(
                        new BasicNameValuePair("scope", scopes),
                        new BasicNameValuePair("client_id", googleDriveConfiguration.getClientId()),
                        new BasicNameValuePair("response_type", "code"),
                        new BasicNameValuePair("redirect_uri", googleDriveConfiguration.getRedirectUrl())
                ), "UTF-8");
        // TODO bash this into configuration.
        return "https://accounts.google.com/o/oauth2/v2/auth?" + query;
    }

    public SourceType getType() {
        return SourceType.GOOGLEDRIVE;
    }

    private GoogleAuthorizationCodeFlow getFlow() throws IOException {
        JsonFactory jsonFactory = new JacksonFactory();
        NetHttpTransport transport = null;
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            // TODO should fail here or something.
            log.error(e.getMessage());
        }

        return new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, googleDriveConfiguration.getClientId(),
                googleDriveConfiguration.getClientSecret(), SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }
}
