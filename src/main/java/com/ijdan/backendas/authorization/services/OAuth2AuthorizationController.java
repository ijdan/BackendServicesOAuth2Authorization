package com.ijdan.backendas.authorization.services;

import com.ijdan.backendas.authorization.entities.BackendServiceAuthorizations;
import com.ijdan.backendas.authorization.repository.IBackendServiceAuthorizationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OAuth2AuthorizationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthorizationController.class);

    @Autowired
    private IBackendServiceAuthorizationsRepository IBackendServiceAuthorizationsRepository;

    public OAuth2AuthorizationController() {
    }

    public List<BackendServiceAuthorizations> getServiceAuthorisations(String clientId){
        return IBackendServiceAuthorizationsRepository.findByClientId(clientId);
    }

}
