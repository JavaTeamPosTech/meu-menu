package com.postechfiap.meumenu.core.domain.presenters;

public interface AlterarSenhaOutputPort {
    void presentSuccess(String message);
    void presentError(String message);
}