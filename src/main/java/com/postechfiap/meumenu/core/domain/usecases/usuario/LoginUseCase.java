package com.postechfiap.meumenu.core.domain.usecases.usuario;

import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;

public interface LoginUseCase {
    String execute(LoginInputModel input);
}