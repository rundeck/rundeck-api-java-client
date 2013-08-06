package org.rundeck.api;

import org.rundeck.api.util.AssertUtil;


/**
 * Builder to build a {@link RundeckClient}, you must specify a url, and at least one of (login, password), token, or
 * sessionId.
 */
public class RundeckClientBuilder {
    private String url;
    private String login;
    private String password;
    private String token = null;
    private String id = null;
    private int version = -1;

    RundeckClientBuilder(){

    }
    /**
     * Specify the URL
     */
    public RundeckClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RundeckClientBuilder login(String login) {
        this.login = login;
        return this;
    }

    public RundeckClientBuilder login(String login, String password) {
        this.login = login;
        this.password = password;
        return this;
    }

    public RundeckClientBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Specify a Rundeck API Token string for authentication
     */
    public RundeckClientBuilder token(String token) {
        this.token = token;
        return this;
    }

    /**
     * Specify a web session ID string for authentication
     */
    public RundeckClientBuilder sessionId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Specify another version number to use
     */
    public RundeckClientBuilder version(final RundeckClient.Version version) {
        this.version = version.getVersionNumber();
        return this;
    }

    /**
     * Specify another version number to use
     */
    public RundeckClientBuilder version(final int version) {
        this.version = version;
        return this;
    }

    /**
     * Create the RundeckClient instance
     */
    public RundeckClient build() {
        if (null == url) {
            AssertUtil.notBlank(url, "The Rundeck URL is required");
        }
        final RundeckClient client = new RundeckClient(url);
        if (null != login && null != password) {
            AssertUtil.notBlank(login, "login cannot be blank");
            AssertUtil.notBlank(password, "password cannot be blank");
            client.setLogin(login);
            client.setPassword(password);
        } else if (null != token) {
            AssertUtil.notBlank(token, "token cannot be blank");
            client.setToken(token);
        } else if (null != id) {
            AssertUtil.notBlank(token, "sessionId cannot be blank");
            client.setSessionID(id);
        } else {
            throw new IllegalStateException("login/password, token, or sessionID must be specified");
        }
        if (version > 0) {
            client.setApiVersion(version);
        }
        return client;
    }
}
