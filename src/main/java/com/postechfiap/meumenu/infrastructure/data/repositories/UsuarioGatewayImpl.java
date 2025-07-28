package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioSpringRepository usuarioSpringRepository;

    @Override
    public boolean existsByLogin(String login) {
        return usuarioSpringRepository.findByLogin(login).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioSpringRepository.findByEmail(email).isPresent();
    }
}