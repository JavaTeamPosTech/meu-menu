package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.LoginInputPort;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginInputPortImpl implements LoginInputPort {

    private final LoginUseCase loginUseCase;

    @Override
    public void execute(LoginInputModel input) {
        loginUseCase.execute(input);
    }
}