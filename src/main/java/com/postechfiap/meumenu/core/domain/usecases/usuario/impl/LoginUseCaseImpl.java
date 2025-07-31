package com.postechfiap.meumenu.core.domain.usecases.usuario.impl;

import com.postechfiap.meumenu.core.domain.presenters.usuario.LoginOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.security.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LoginOutputPort loginOutputPort;

    @Override
    public String execute(LoginInputModel input) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(input.getLogin(), input.getSenha());
            Authentication auth = authenticationManager.authenticate(authToken);
            String token = tokenService.generateToken(auth.getName());
            loginOutputPort.presentSuccess(token);
            return token;

        } catch (AuthenticationException e) {
            loginOutputPort.presentError("Credenciais inválidas: " + e.getMessage());
            throw new BusinessException("Credenciais inválidas.");
        }
    }
}