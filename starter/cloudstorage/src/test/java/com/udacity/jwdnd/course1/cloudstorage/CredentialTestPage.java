package com.udacity.jwdnd.course1.cloudstorage;

public class CredentialTestPage {
    private String credentialUrl;
    private String credentialUsername;
    private String credentialPassword;

    public CredentialTestPage(String credentialUrl, String credentialUsername, String credentialPassword) {
        this.credentialUrl = credentialUrl;
        this.credentialUsername = credentialUsername;
        this.credentialPassword = credentialPassword;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getCredentialUsername() {
        return credentialUsername;
    }

    public void setCredentialUsername(String credentialUsername) {
        this.credentialUsername = credentialUsername;
    }

    public String getCredentialPassword() {
        return credentialPassword;
    }

    public void setCredentialPassword(String credentialPassword) {
        this.credentialPassword = credentialPassword;
    }
}
