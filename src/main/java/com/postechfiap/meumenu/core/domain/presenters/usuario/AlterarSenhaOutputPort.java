package com.postechfiap.meumenu.core.domain.presenters.usuario;

public interface AlterarSenhaOutputPort {
    void presentSuccess(String message);
    void presentError(String message);
}