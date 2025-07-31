package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;

public interface RestauranteBasicDataMapper {

    RestauranteEntity toBasicEntity(RestauranteDomain domain);
    RestauranteDomain toBasicDomain(RestauranteEntity entity);
}