package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ProprietarioSpringRepository extends JpaRepository<ProprietarioEntity, UUID> {
    Optional<ProprietarioEntity> findByCpf(String cpf);
}