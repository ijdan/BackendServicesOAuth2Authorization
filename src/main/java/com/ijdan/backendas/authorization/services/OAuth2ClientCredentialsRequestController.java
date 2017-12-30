/**
 * Cette class se charge d'effectuer les principaux contrôles permettant de valider la requête.
 * Le flow Client Credentials Grant flow est uniquement toléré
 * Les contrôles à faire sont :
 * The request is missing a required parameter
 * includes an unsupported parameter value (other than grant type) repeats a parameter
 * includes multiple credentials
 * utilizes more than one mechanism for authenticating the client, or is otherwise malformed
 *
 * */


package com.ijdan.backendas.authorization.services;

import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponseDetail;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;


@Component
public class OAuth2ClientCredentialsRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientCredentialsRequestController.class);
    private static final String[] acceptedParameters = new String[]{"grant_type", "scope", "client_id", "client_secret"};

    @Autowired
    private OAuth2BasicAuthenticationController authen;

    public OAuth2ClientCredentialsRequestController() {

    }

    public OAuth2ClientCredentialsErrorResponseDetail scanRequest (MultiValueMap<String, String> allParams, String authorization){
        /**
         * Est-ce que les paramètres requis sont manquant ?
         * grant_type   : REQUIRED.  Value MUST be set to "client_credentials".
         * scope        : OPTIONAL.  The scope of the access request as described by
         * */
        if (!allParams.containsKey("grant_type")){
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "<grant_type> parameter omitted",
                    "");
        }
        /**
         * grant_type=client_credentials
         * */
        if (!allParams.getFirst("grant_type").equals("client_credentials")){
            LOGGER.warn("allParams.getFirst(\"grant_type\") >>" + allParams.getFirst("grant_type"));
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.UNSUPPORTED_GRANT_TYPE,
                    "Only <client_credentials> is supported",
                    "");
        }

        /**
         * repeats a parameter
         * */
        for(int i = 0; i < acceptedParameters.length; i++){
            if (allParams.getOrDefault(acceptedParameters[i], new ArrayList<>()).size() > 1) {
                return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Plusieurs occurences trouvées pour {"+ acceptedParameters[i] +"}",
                        "");
            }
        }

        /**
         * includes an unsupported parameter value
         * */
        for(String k : allParams.keySet()){
            if (!Arrays.asList(acceptedParameters).contains(k)){
                return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Paramètre non supporté {"+ k +"}",
                        "");
            }
        }

        if(authorization == null) {
            return new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "Requête non authentifiée !",
                    "");
        }
        LOGGER.warn("authorization : {}", authorization);

        //success
        return null;
    }


}
