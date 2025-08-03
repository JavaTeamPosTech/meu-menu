package com.postechfiap.meumenu.core.domain.presenters.cliente;

public interface DeletarClienteOutputPort {
    void presentSuccess(String message);

    void presentError(String message);

    boolean hasError();
}
