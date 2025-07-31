package com.postechfiap.meumenu.core.controllers.usuario;

import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;

public interface LoginInputPort {
    void execute(LoginInputModel input);
}