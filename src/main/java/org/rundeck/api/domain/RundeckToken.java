package org.rundeck.api.domain;

/**
 * RundeckToken is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-04-04
 */
public class RundeckToken {
    private String user;
    private String token;

    public RundeckToken() {
    }

    public RundeckToken(String user, String token) {
        this.setUser(user);
        this.setToken(token);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
