package com.ijdan.backendas.authorization.infra.db.cache.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "services_authorization")
public class BackendServiceAuthorizations {
    @Id
    private String id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "producer_id", nullable = false)
    private String producerId;

    @Column(name = "scope", nullable = false)
    private String scope;

    public BackendServiceAuthorizations() {
        //for JPA
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "BackendServiceAuthorizations{" +
                "id='" + id + '\'' +
                ", clientId='" + clientId + '\'' +
                ", producerId='" + producerId + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
