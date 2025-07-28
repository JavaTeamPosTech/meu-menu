package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface RestauranteSpringRepository extends JpaRepository<RestauranteEntity, UUID> {
    Optional<RestauranteEntity> findByCnpj(String cnpj);
}