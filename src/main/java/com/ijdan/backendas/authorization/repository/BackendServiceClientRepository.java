package com.ijdan.backendas.authorization.repository;

import com.ijdan.backendas.authorization.entities.BackendServiceClient;
import org.springframework.data.repository.CrudRepository;

public interface BackendServiceClientRepository extends CrudRepository<BackendServiceClient, String> {
    BackendServiceClient findByClientIdAndClientSecret(String clientId, String clientSecret);
    Long countByClientId(String clientId);
}