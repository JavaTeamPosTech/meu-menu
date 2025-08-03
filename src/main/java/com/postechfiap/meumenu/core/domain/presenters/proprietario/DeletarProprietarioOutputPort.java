package com.postechfiap.meumenu.core.domain.presenters.proprietario;

public interface DeletarProprietarioOutputPort {
    void presentSuccess(String message);

    void presentError(String message);

    boolean hasError();
}