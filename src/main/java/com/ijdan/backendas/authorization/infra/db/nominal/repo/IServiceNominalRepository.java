package com.ijdan.backendas.authorization.infra.db.nominal.repo;

import com.ijdan.backendas.authorization.infra.db.IBackendServiceClientProjection;
import com.ijdan.backendas.authorization.infra.db.nominal.domain.BackendServiceClient;
import org.springframework.data.repository.Repository;

import java.util.Collection;

public interface IServiceNominalRepository extends Repository<BackendServiceClient, String> {
    IBackendServiceClientProjection findByClientIdAndClientSecret(String clientId, String clientSecret);

    Collection<IBackendServiceClientProjection> findAll();

    Collection<IBackendServiceClientProjection> findByClientId(String clientId);

    Long countByClientId(String clientId);
}