package com.postechfiap.meumenu.core.controllers.usuario.impl;

import com.postechfiap.meumenu.core.controllers.usuario.LoginInputPort;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginInputPortImpl implements LoginInputPort {

    private final LoginUseCase loginUseCase;

    @Override
    public void execute(LoginInputModel input) {
        loginUseCase.execute(input);
    }
}