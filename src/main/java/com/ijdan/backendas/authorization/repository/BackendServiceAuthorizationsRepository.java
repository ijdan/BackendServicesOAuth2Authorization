package com.ijdan.backendas.authorization.repository;

import com.ijdan.backendas.authorization.entities.BackendServiceAuthorizations;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BackendServiceAuthorizationsRepository extends CrudRepository<BackendServiceAuthorizations, String> {

    List<BackendServiceAuthorizations> findByClientId(String clientId);
    List<BackendServiceAuthorizations> findAll();

}
