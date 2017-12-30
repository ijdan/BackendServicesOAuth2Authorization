package com.ijdan.backendas.authorization.services;

import java.util.Base64;

public class HttpBasicAuthenticationCredentials {
    private String clientId;
    private String clientSecret;

    public HttpBasicAuthenticationCredentials (String authorizationHeader) {
        String Base64Credentials = authorizationHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(Base64Credentials);
        String[] credentials = new String(decodedBytes).split(":");
        if (credentials.length == 2){
            this.clientId = credentials[0];
            this.clientSecret = credentials[1];
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
