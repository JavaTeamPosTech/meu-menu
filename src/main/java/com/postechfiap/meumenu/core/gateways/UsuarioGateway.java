package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    Optional<UsuarioDomain> buscarUsuarioPorId(UUID id);
    UsuarioDomain atualizarUsuario(UsuarioDomain usuarioDomain);
}