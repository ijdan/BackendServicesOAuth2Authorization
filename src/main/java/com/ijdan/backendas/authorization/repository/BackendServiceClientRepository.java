package com.ijdan.backendas.authorization.repository;

import com.ijdan.backendas.authorization.entities.BackendServiceClient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BackendServiceClientRepository extends CrudRepository<BackendServiceClient, String> {
    @Query("SELECT t FROM BackendServiceClient t where t.clientId = ?1 AND t.clientSecret = ?2")
    List<BackendServiceClient> findByClientIdClientSecret(String clientId, String clientSecret);
}