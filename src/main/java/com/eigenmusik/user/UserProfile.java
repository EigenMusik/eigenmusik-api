package com.eigenmusik.user;

import com.eigenmusik.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Account account;

    private String displayName;

    protected UserProfile() {

    }

    public UserProfile(Account account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
