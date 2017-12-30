package com.ijdan.backendas.authorization.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ijdan.backendas.authorization.controllers.OAuth2ClientCredentialsTokenEndpoint;
import com.ijdan.backendas.authorization.entities.BackendServiceAuthorizations;
import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientCredentialsTokenEndpoint.class);
    private static final String TYPE = "bearer";
    private String access_token;
    private Long expires_in;
    private String scope;
    private String token_type;

    private static final long EXPIRATION_TIME = 3600;
    private static final String ISSUER = "https://www.authorization-server.com";
    private static final String CERTIFICATE_PATH = "certificate/sa_certificate.p12";
    private static final String PASSWORD = "pass";

    public OAuth2SuccessResponse(String clientId, List<BackendServiceAuthorizations> bSA) throws ExceptionsHandller {
        scope = "";
        token_type = TYPE;
        Instant now = Instant.now();
        expires_in = now.toEpochMilli() / 1000;
        List<String> producersAuthorized = new ArrayList<String>();

        for (BackendServiceAuthorizations pAuthZ : bSA){
            producersAuthorized.add(pAuthZ.getProducerId());
        }

        try (FileInputStream fis = new FileInputStream(CERTIFICATE_PATH)) {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, PASSWORD.toCharArray());
            Enumeration<String> aliases = ks.aliases();

            if (aliases == null || !aliases.hasMoreElements()) {
                LOGGER.error("Absence de certificat !!!! ");
                throw new ExceptionsHandller("Problème d'accès au certificat paramétré !");
            }

            String alias = aliases.nextElement();

            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
                    new KeyStore.PasswordProtection(PASSWORD.toCharArray()));

            RSAPrivateKey privateKey = (RSAPrivateKey) keyEntry.getPrivateKey();

            RSAPublicKey publicKey = (RSAPublicKey) keyEntry.getCertificate().getPublicKey();

            access_token = JWT.create()
                    .withIssuer(ISSUER)
                    .withAudience("["+ String.join(", ", producersAuthorized) +"]")
                    .withExpiresAt(Date.from(now.plusSeconds(EXPIRATION_TIME)))
                    .withIssuedAt(Date.from(now))
                    .withSubject(clientId)
                    .withNotBefore(Date.from(now))
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(Algorithm.RSA256(publicKey, privateKey));

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
                | UnrecoverableEntryException ex) {
            LOGGER.error("Exception !!!! {}", ex.getMessage());
            throw new ExceptionsHandller("Could not initialize OAuth Service", ex);
        }

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
