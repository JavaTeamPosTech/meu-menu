package com.postechfiap.meumenu.core.domain.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

public interface AtualizarClienteOutputPort {
    void presentSuccess(ClienteDomain cliente);

    void presentError(String message);

    String getErrorMessage();
}
