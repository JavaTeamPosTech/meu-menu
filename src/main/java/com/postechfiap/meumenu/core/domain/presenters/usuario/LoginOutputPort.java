package com.postechfiap.meumenu.core.domain.presenters.usuario;

public interface LoginOutputPort {
    void presentSuccess(String token);
    void presentError(String message);
}