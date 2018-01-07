package com.ijdan.backendas.authorization.controllers;

import com.ijdan.backendas.authorization.infra.db.IBackendServiceClientProjection;
import com.ijdan.backendas.authorization.infra.db.nominal.domain.BackendServiceAuthorizations;
import com.ijdan.backendas.authorization.errors.OAuth2ClientCredentialsErrorResponse;
import com.ijdan.backendas.authorization.exception.ExceptionsHandller;
import com.ijdan.backendas.authorization.model.Result;
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

import java.io.IOException;
import java.text.ParseException;
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
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) throws ExceptionsHandller {

        Result result = null;
        try {
            //Request validator
            result = oAuth2ClientCredentialsRequestController.scanRequest(requestParameters, authorization);
            if (!result.isSuccess()){
                return new ResponseEntity<>(result.getBody(), HttpStatus.BAD_REQUEST);
            }

            //Authentication
            result = oAuth2BasicAuthenticationController.scanRequest(requestParameters, authorization);
            if (!result.isSuccess()){
                return new ResponseEntity<>(result.getBody(), HttpStatus.UNAUTHORIZED);
            }

            //Authorization
            IBackendServiceClientProjection ibscp = (IBackendServiceClientProjection) result.getBody();
            String clientId = ibscp.getClientId();

            List<BackendServiceAuthorizations> bSA = oAuth2AuthorizationController.getServiceAuthorisations(clientId);
            if (bSA.size() == 0){
                //authorization not found
                OAuth2ClientCredentialsErrorResponseDetail body = new OAuth2ClientCredentialsErrorResponseDetail(OAuth2ClientCredentialsErrorResponse.UNAUTHORIZED_CLIENT,
                        "Unauthorized Client <"+clientId+">.",
                        "");
                return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
            }else{
                //access_token
                return new ResponseEntity<>(new OAuth2SuccessResponse(clientId, bSA), HttpStatus.OK);
            }
        }catch (IOException | DataFormatException | ParseException ex) {
            throw new ExceptionsHandller("Could not initialize OAuth Service.", ex);
        }
    }
}
