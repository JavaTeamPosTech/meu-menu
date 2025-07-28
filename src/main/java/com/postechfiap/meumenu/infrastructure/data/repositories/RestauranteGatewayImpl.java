package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteDataMapper;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;



@Component
@RequiredArgsConstructor
public class RestauranteGatewayImpl implements RestauranteGateway {

    private final RestauranteSpringRepository restauranteSpringRepository;
    private final RestauranteDataMapper restauranteDataMapper;

    @Override
    public RestauranteDomain cadastrarRestaurante(RestauranteDomain restauranteDomain) {
        RestauranteEntity restauranteEntity = restauranteDataMapper.toEntity(restauranteDomain);
        RestauranteEntity savedEntity = restauranteSpringRepository.save(restauranteEntity);
        return restauranteDataMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return restauranteSpringRepository.findByCnpj(cnpj).isPresent();
    }
}