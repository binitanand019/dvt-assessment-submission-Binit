package com.binit.flightrewards.dto;

public class LoginResponse {

    private String token;
    private int expiresIn;
    private String tokenType;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            int expiresIn,
            String tokenType
    ) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}