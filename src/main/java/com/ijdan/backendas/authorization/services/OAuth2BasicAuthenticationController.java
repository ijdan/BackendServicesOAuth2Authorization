/**
 * Seulement Basic authentication est accpetée
 * Les credentials sont recherchés dans le Header ou dans le body de la requête
 * */
package com.ijdan.backendas.authorization.services;

import com.ijdan.backendas.authorization.entities.BackendServiceClient;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponseDetail;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponse;
import com.ijdan.backendas.authorization.repository.BackendServiceClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

@Service
public class OAuth2BasicAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2BasicAuthenticationController.class);
    private static final int DELAY_EXPIRATION_CREDENTIALS = 86400;

    private static final long CREDENTIAL_EXPIRATION_DAYS = 1;

    String HttpClientId;
    @Autowired
    private BackendServiceClientRepository backendServiceClientRepository;

    public OAuth2BasicAuthenticationController() {
    }

    public OAuth2ClientCredentialsErrorResponseDetail scanRequest (MultiValueMap<String, String> requestParameters,
                                                                   String authorization)
            throws IOException, DataFormatException, ParseException {

        if (authorization == null || !authorization.startsWith("Basic ")) {
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Basic authentication manquante", "");
        }

        HttpBasicAuthenticationCredentials httpBasicAuthenticationCredentials = new HttpBasicAuthenticationCredentials(authorization);

        String clientId = httpBasicAuthenticationCredentials.getClientId();
        this.HttpClientId = clientId;
        String clientSecret = httpBasicAuthenticationCredentials.getClientSecret();

        /**
         * Check Credentials format
         * */
        if (clientId.equals("") || clientSecret.equals("")){
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Credentials non conformes", "");
        }
        /**
         * Check authentication
         * */
        BackendServiceClient backendServiceClient = backendServiceClientRepository.findByClientIdAndClientSecret(clientId, clientSecret);
        if (backendServiceClient == null){
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_CLIENT,
                        "Authentification échouée", "");
        }

        /**
         * Si plusieurs credentials associés alors si seulement les derniers peuvent être utilisés au-dela de DELAY_EXPIRATION_CREDENTIALS (secondes)
         * */
        if (backendServiceClient.getReplaced().equals("1")){
            SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateUpdated = datePattern.parse(backendServiceClient.getUpdated());
            Date now = new Date();
            long daysBefore = TimeUnit.DAYS.convert(now.getTime() - dateUpdated.getTime(), TimeUnit.MILLISECONDS);
            if (daysBefore > CREDENTIAL_EXPIRATION_DAYS){
                return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_CLIENT,
                        "Enabled credentials", "");
            }

        }

        /**
         * Récupère l'élément trouvé
         * */
        return null;
    }

    public String getHttpClientId() {
        return HttpClientId;
    }

    public void setHttpClientId(String httpClientId) {
        HttpClientId = httpClientId;
    }
}
