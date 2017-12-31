/**
 * Seulement Basic authentication est accpetée
 * Les credentials sont recherchés dans le Header ou dans le body de la requête
 * */
package com.ijdan.backendas.authorization.services;

import com.ijdan.backendas.authorization.entities.BackendServiceClient;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponseDetail;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponse;
import com.ijdan.backendas.authorization.model.Result;
import com.ijdan.backendas.authorization.repository.IBackendServiceClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

@Service
public class OAuth2BasicAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2BasicAuthenticationController.class);

    private static final long DELAY_EXPIRATION_CREDENTIALS = 1;

    @Autowired
    private IBackendServiceClientRepository IBackendServiceClientRepository;

    public OAuth2BasicAuthenticationController() {
    }

    public Result scanRequest (MultiValueMap<String, String> requestParameters,
                               String authorization)
            throws IOException, DataFormatException, ParseException {

        if (authorization == null || !authorization.startsWith("Basic ")) {
            OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "Basic authentication missing.",
                    "");
            return Result.fail(body);
        }

        HttpBasicAuthenticationCredentials httpBasicAuthenticationCredentials = new HttpBasicAuthenticationCredentials(authorization);

        String clientId = httpBasicAuthenticationCredentials.getClientId();
        String clientSecret = httpBasicAuthenticationCredentials.getClientSecret();

        /**
         * Check Credentials format
         * */
        if (clientId.equals("") || clientSecret.equals("")){
            OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "Wrong credentails <"+ clientId +">.",
                    "");
            return Result.fail(body);
        }
        /**
         * Check authentication
         * */
        BackendServiceClient backendServiceClient = IBackendServiceClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
        if (backendServiceClient == null){
            OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_CLIENT,
                    "Authentication failed <"+ clientId +">.",
                    "");
            return Result.fail(body);
        }

        /**
         * Check if credentials are expired
         * DELAY_EXPIRATION_CREDENTIALS
         * */
        if (backendServiceClient.getReplaced().equals("1")){
            SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateUpdated = datePattern.parse(backendServiceClient.getUpdated());
            Date now = new Date();
            long daysBefore = TimeUnit.DAYS.convert(now.getTime() - dateUpdated.getTime(), TimeUnit.MILLISECONDS);
            if (daysBefore > DELAY_EXPIRATION_CREDENTIALS){
                OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_CLIENT,
                        "Enabled credentials for <"+ clientId +">",
                        "");
                return Result.fail(body);
            }
        }

        /**
         * Return Client object
         * */
        return Result.success(backendServiceClient);
    }

}
