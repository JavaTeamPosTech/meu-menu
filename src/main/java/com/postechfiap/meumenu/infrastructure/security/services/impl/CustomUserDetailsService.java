package com.postechfiap.meumenu.infrastructure.security.services.impl;

import com.postechfiap.meumenu.infrastructure.data.repositories.UsuarioSpringRepository; // O Spring Data JPA Repository
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioSpringRepository usuarioSpringRepository; // Injeta o seu JPA Repository

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioSpringRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
    }
}