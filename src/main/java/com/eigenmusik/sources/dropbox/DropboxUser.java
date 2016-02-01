package com.eigenmusik.sources.dropbox;

import com.dropbox.core.v2.DbxUsers;
import com.eigenmusik.sources.googledrive.GoogleDriveAccessToken;
import com.google.api.services.oauth2.model.Userinfoplus;

import javax.persistence.*;

@Entity
public class DropboxUser {


    @Id
    @GeneratedValue
    private Long id;
    private String dropboxId;
    private String email;
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private DropboxAccessToken accessToken;

    public DropboxUser(DbxUsers.FullAccount currentAccount) {
        this.dropboxId = currentAccount.accountId;
        this.email = currentAccount.email;
    }

    public void setAccessToken(DropboxAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }
}
