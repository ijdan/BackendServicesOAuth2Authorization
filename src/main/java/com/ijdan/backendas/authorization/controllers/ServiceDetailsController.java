package com.ijdan.backendas.authorization.controllers;

import com.ijdan.backendas.authorization.infra.db.CacheSwitcher;
import com.ijdan.backendas.authorization.infra.db.IBackendServiceClientProjection;
import com.ijdan.backendas.authorization.infra.db.cache.repo.IServiceCacheRepository;
import com.ijdan.backendas.authorization.infra.db.nominal.repo.IServiceNominalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1.0")
public class ServiceDetailsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDetailsController.class);

    @Autowired private IServiceNominalRepository nominalRepository;
    @Autowired private IServiceCacheRepository cacheRepository;

    @RequestMapping(value = "/services",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity getServices(){
        Collection<IBackendServiceClientProjection> ibscp = nominalRepository.findAll();
        return new ResponseEntity<>(ibscp, HttpStatus.OK);
    }

    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity getService(@PathVariable(value="id") String id){
        Collection<IBackendServiceClientProjection> ibscp = nominalRepository.findByClientId(id);
        if (ibscp.size() > 0) {
            return new ResponseEntity<>(ibscp, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
