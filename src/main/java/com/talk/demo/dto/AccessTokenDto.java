package com.talk.demo.dto;

public class AccessTokenDto {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirected_uri;
    private String state;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirected_uri() {
        return redirected_uri;
    }

    public void setRedirected_uri(String redirected_uri) {
        this.redirected_uri = redirected_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
