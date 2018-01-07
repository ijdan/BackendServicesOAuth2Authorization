package com.ijdan.backendas.authorization.infra.db.cache.repo;

import com.ijdan.backendas.authorization.infra.db.cache.domain.BackendServiceClient;
import com.ijdan.backendas.authorization.infra.db.IBackendServiceClientProjection;
import org.springframework.data.repository.Repository;

import java.util.Collection;

public interface IServiceCacheRepository extends Repository<BackendServiceClient, String> {
    IBackendServiceClientProjection findByClientIdAndClientSecret(String clientId, String clientSecret);

    Collection<IBackendServiceClientProjection> findAll();

    Collection<IBackendServiceClientProjection> findByClientId(String clientId);

    Long countByClientId(String clientId);
}