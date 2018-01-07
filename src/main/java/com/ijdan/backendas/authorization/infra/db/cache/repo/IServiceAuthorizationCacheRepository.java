package com.ijdan.backendas.authorization.infra.db.cache.repo;

import com.ijdan.backendas.authorization.infra.db.cache.domain.BackendServiceAuthorizations;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface IServiceAuthorizationCacheRepository extends Repository<BackendServiceAuthorizations, String> {

    List<BackendServiceAuthorizations> findByClientId(String clientId);
    List<BackendServiceAuthorizations> findAll();

}
