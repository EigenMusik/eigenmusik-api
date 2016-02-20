package com.eigenmusik.api.sources.dropbox;

import com.dropbox.core.DbxSessionStore;

/**
 * A session store implementation that I'm using to spoof DropBox for the time being.
 * TODO figure out Session storage with Spring.
 */
public class DropboxUserSessionStore implements DbxSessionStore {

    public final static String fakeCsrfToken = "FAKESESSIONTOKENBECAUSEIDONTREALLYCAREABOUTXRSSFATTHISPOINT";

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
