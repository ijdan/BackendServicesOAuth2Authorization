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
import com.ijdan.backendas.authorization.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;


@Service
public class OAuth2ClientCredentialsRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientCredentialsRequestController.class);
    private static final String[] acceptedParameters = new String[]{"grant_type", "scope", "client_id", "client_secret"};

    @Autowired
    private OAuth2BasicAuthenticationController authen;

    public OAuth2ClientCredentialsRequestController() {

    }

    public Result scanRequest (MultiValueMap<String, String> allParams, String authorization){
        /**
         * Est-ce que les paramètres requis sont manquant ?
         * grant_type   : REQUIRED.  Value MUST be set to "client_credentials".
         * scope        : OPTIONAL.  The scope of the access request as described by
         * */
        if (!allParams.containsKey("grant_type")){
            OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "<grant_type> parameter omitted",
                    "");
            return Result.fail(body);
        }
        /**
         * grant_type=client_credentials
         * */
        if (!allParams.getFirst("grant_type").equals("client_credentials")){
            OAuth2ClientCredentialsErrorResponseDetail body =  new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.UNSUPPORTED_GRANT_TYPE,
                    "Only <client_credentials> is supported",
                    "");
            return Result.fail(body);
        }

        /**
         * repeats a parameter
         * */
        for(int i = 0; i < acceptedParameters.length; i++){
            if (allParams.getOrDefault(acceptedParameters[i], new ArrayList<>()).size() > 1) {
                OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Several occurrences found for {"+ acceptedParameters[i] +"}",
                        "");
                return Result.fail(body);
            }
        }

        /**
         * includes an unsupported parameter value
         * */
        for(String k : allParams.keySet()){
            if (!Arrays.asList(acceptedParameters).contains(k)){
                OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                        "Not supported parameter {"+ k +"}",
                        "");
                return Result.fail(body);
            }
        }

        if(authorization == null) {
            OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.INVALID_REQUEST,
                    "Not authenticated request !",
                    "");
            return Result.fail(body);
        }

        //success
        return Result.success();
    }


}
