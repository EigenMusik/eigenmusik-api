package com.eigenmusik.sources.dropbox;

import com.dropbox.core.DbxSessionStore;

public class DropboxUserSessionStore implements DbxSessionStore {

    public final static String fakeCsrfToken = "FAKESESSIONTOKENBECAUSEIDONTREALLYCAREABOUTXRSFATTHISPOINT";

    public DropboxUserSessionStore() {
    }

    @Override
    public String get() {
        return fakeCsrfToken;
    }

    @Override
    public void set(String s) {

    }

    @Override
    public void clear() {

    }
}
