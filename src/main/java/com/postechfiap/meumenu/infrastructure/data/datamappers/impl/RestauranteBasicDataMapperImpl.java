package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteBasicDataMapper;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import org.springframework.stereotype.Component;

@Component
public class RestauranteBasicDataMapperImpl implements RestauranteBasicDataMapper {

    @Override
    public RestauranteEntity toBasicEntity(RestauranteDomain domain) {
        if (domain == null) return null;

        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(domain.getId());
        entity.setCnpj(domain.getCnpj());
        entity.setRazaoSocial(domain.getRazaoSocial());
        entity.setNomeFantasia(domain.getNomeFantasia());
        entity.setInscricaoEstadual(domain.getInscricaoEstadual());
        entity.setTelefoneComercial(domain.getTelefoneComercial());
        return entity;
    }

    @Override
    public RestauranteDomain toBasicDomain(RestauranteEntity entity) {
        if (entity == null) return null;

        RestauranteDomain domain = new RestauranteDomain();
        domain.setId(entity.getId());
        domain.setCnpj(entity.getCnpj());
        domain.setRazaoSocial(entity.getRazaoSocial());
        domain.setNomeFantasia(entity.getNomeFantasia());
        domain.setInscricaoEstadual(entity.getInscricaoEstadual());
        domain.setTelefoneComercial(entity.getTelefoneComercial());
        return domain;
    }
}