package com.ijdan.backendas.authorization.controllers;

import com.ijdan.backendas.authorization.entities.BackendServiceAuthorizations;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponse;
import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
import com.ijdan.backendas.authorization.services.OAuth2BasicAuthenticationController;
import com.ijdan.backendas.authorization.services.OAuth2AuthorizationController;
import com.ijdan.backendas.authorization.services.OAuth2ClientCredentialsRequestController;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponseDetail;
import com.ijdan.backendas.authorization.services.OAuth2SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping(value = "/v1.0/oauth")
public class OAuth2ClientCredentialsTokenEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientCredentialsTokenEndpoint.class);

    @Autowired
    private OAuth2ClientCredentialsRequestController oAuth2ClientCredentialsRequestController;

    @Autowired
    private OAuth2BasicAuthenticationController oAuth2BasicAuthenticationController;

    @Autowired
    private OAuth2AuthorizationController oAuth2AuthorizationController;



    /**
     * Client Credentials Grant Flow
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity getAccessToken(
            @RequestParam MultiValueMap<String, String> requestParameters,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            HttpServletRequest httpRequest
    ) throws ExceptionsHandller {
        OAuth2ClientCredentialsErrorResponseDetail err = null;
        try {
            err = oAuth2ClientCredentialsRequestController.scanRequest(requestParameters, authorization);
            if (err != null){
                //Requête mal construite
                return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
            }

            err = oAuth2BasicAuthenticationController.scanRequest(requestParameters, authorization);
            if (err != null){
                //Erreur dans la partie credentials
                return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
            }
            /**
             * Authentification réussie
             * Récupération des autorisations.
             * 2 cas : récupère autorisation et aucune autorisation
             * */
            String clientId = oAuth2BasicAuthenticationController.getHttpClientId();
            List<BackendServiceAuthorizations> bSA = oAuth2AuthorizationController.getServiceAuthorisations(clientId);
            if (bSA.size() > 0){
                /**
                 * Autorisations trouvées.
                 * Création de l'access_token associé
                 * */
                return new ResponseEntity<>(new OAuth2SuccessResponse(clientId, bSA), HttpStatus.OK);
            }else{
                err = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.UNAUTHORIZED_CLIENT,
                        "Unauthorized Client <"+clientId+">",
                        "");
                return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
            }
        }catch (IOException | DataFormatException ex) {
            throw new ExceptionsHandller("Could not initialize OAuth Service", ex);
        }
    }
}
