package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioSpringRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByLogin(String login);
    Optional<UsuarioEntity> findByEmail(String email);
}
