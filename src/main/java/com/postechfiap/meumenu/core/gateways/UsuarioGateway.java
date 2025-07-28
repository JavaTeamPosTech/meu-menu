package com.postechfiap.meumenu.core.gateways;

public interface UsuarioGateway {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
