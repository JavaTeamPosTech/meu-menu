package com.postechfiap.meumenu.core.domain.presenters;

public interface LoginOutputPort {
    void presentSuccess(String token);
    void presentError(String message);
}