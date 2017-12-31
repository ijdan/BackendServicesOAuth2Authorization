package com.ijdan.backendas.authorization.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@ConfigurationProperties(prefix = "access_token")
@Component
public class OAuth2AccessToken {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AccessToken.class);

    private static String DEFAULT_CURRENT_KID = "e9bc097a-ce51-4036-9562-d2ade882db0d";
    @Value("${key_id}")
    private static String keyId;

    private static final int DEFAULT_EXPIRATION_TIME = 10;
    @Value("${expiration_time}")
    private static String expirationTime;


    private static final String DEFAULT_ISSUER = "a-s.com";
    @Value("${issuer}")
    private static String issuer;

    public OAuth2AccessToken() {
    }

    public String getNewOne(String clientId, List<String> producersAuthorized) throws ExceptionsHandller {
        CertificateManager.Keys keys = new CertificateManager().keys();
        if (keys != null) {
            Instant now = Instant.now();

            keyId = keyId == null ? String.valueOf(DEFAULT_CURRENT_KID) : keyId;
            Map<String, Object> header = new HashMap<>();
            header.put("kid", keyId);

            String jti = UUID.randomUUID().toString();

            expirationTime = expirationTime == null ? String.valueOf(DEFAULT_EXPIRATION_TIME) : expirationTime;
            long exp = Integer.valueOf(expirationTime).longValue();

            issuer = issuer == null ? DEFAULT_ISSUER : issuer;

            LOGGER.info("JWTs kid:<{}> generated with jti:<{}>", keyId, jti);
            return JWT.create()
                    .withHeader(header)

                    .withIssuer(issuer)
                    .withSubject(clientId)
                    .withAudience("[" + String.join(", ", producersAuthorized) + "]")
                    .withExpiresAt(Date.from(now.plusSeconds(exp)))
                    .withJWTId(jti)

                    .sign(Algorithm.RSA256(keys.getPublicKey(), keys.getPrivateKey()));
        }else{
            LOGGER.error("Failed in using certificate");
            return null;
        }

    }

    public static String getKeyId() {
        return keyId;
    }

    public static void setKeyId(String keyId) {
        OAuth2AccessToken.keyId = keyId;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
