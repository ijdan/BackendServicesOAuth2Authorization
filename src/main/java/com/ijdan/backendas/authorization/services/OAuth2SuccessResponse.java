package com.ijdan.backendas.authorization.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ijdan.backendas.authorization.controllers.OAuth2ClientCredentialsTokenEndpoint;
import com.ijdan.backendas.authorization.entities.BackendServiceAuthorizations;
import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
import com.ijdan.backendas.authorization.model.CertificateManager;
import com.ijdan.backendas.authorization.model.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;


public class OAuth2SuccessResponse {
    private String access_token;
    private Long expires_in;
    private String scope;
    private String token_type = "bearer";

    public OAuth2SuccessResponse(String clientId, List<BackendServiceAuthorizations> bSA) throws ExceptionsHandller {
        scope = "";
        expires_in = Instant.now().toEpochMilli() / 1000;

        //Authorized Audience
        List<String> producersAuthorized = new ArrayList<String>();
        for (BackendServiceAuthorizations pAuthZ : bSA){
            producersAuthorized.add(pAuthZ.getProducerId());
        }
        access_token = new OAuth2AccessToken().getNewOne(clientId, producersAuthorized);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
