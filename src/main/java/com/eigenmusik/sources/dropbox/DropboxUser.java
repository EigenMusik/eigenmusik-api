package com.eigenmusik.sources.dropbox;

import com.dropbox.core.v2.DbxUsers;

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

    public DropboxUser() {

    }

    public DropboxUser(DbxUsers.FullAccount currentAccount) {
        this.dropboxId = currentAccount.accountId;
        this.email = currentAccount.email;
    }

    public DropboxAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(DropboxAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setDropboxId(String dropboxId) {
        this.dropboxId = dropboxId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
