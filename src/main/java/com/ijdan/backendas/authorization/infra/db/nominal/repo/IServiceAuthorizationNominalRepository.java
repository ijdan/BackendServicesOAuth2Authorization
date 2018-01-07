package com.ijdan.backendas.authorization.infra.db.nominal.repo;

import com.ijdan.backendas.authorization.infra.db.nominal.domain.BackendServiceAuthorizations;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface IServiceAuthorizationNominalRepository extends Repository<BackendServiceAuthorizations, String> {

    List<BackendServiceAuthorizations> findByClientId(String clientId);
    List<BackendServiceAuthorizations> findAll();

}
