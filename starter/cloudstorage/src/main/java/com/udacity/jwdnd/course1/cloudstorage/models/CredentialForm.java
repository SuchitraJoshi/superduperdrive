package com.udacity.jwdnd.course1.cloudstorage.models;

import org.springframework.boot.autoconfigure.domain.EntityScan;

public class CredentialForm {
    private Integer credentialId;
    private Integer userId;

    private String credentialUrl;
    private String credentialUsername;
    private String credentialPassword;


    public CredentialForm(Integer credentialId, Integer userId, String url, String username, String password) {
        this.credentialId = credentialId;
        this.userId = userId;
        this.credentialUrl = url;
        this.credentialUsername = username;
        this.credentialPassword = password;
    }

    public CredentialForm() {

    }
//    public CredentialForm(Credential credential){
//        this.credentialId= credential.getCredentialId();
//        this.userId = credential.getUserId();
//        this.url = credential.getUrl();
//        this.username = credential.getUsername();
//        this.password = credential.getEncryptedPassword();
//    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
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
