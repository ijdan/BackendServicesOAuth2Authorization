package com.ijdan.backendas.authorization.repository;

import com.ijdan.backendas.authorization.entities.BackendServiceClient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BackendServiceClientRepository extends CrudRepository<BackendServiceClient, String> {
    BackendServiceClient findByClientIdAndClientSecret(String clientId, String clientSecret);
    List<BackendServiceClient> findAll();

    Long countByClientId(String clientId);
}