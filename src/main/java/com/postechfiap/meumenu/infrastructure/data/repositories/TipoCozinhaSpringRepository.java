package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.infrastructure.model.TipoCozinhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface TipoCozinhaSpringRepository extends JpaRepository<TipoCozinhaEntity, UUID> {
    Optional<TipoCozinhaEntity> findByNome(String nome);
}
