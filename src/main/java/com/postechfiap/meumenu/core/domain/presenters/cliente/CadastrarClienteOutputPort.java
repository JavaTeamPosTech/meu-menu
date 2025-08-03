package com.postechfiap.meumenu.core.domain.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

public interface CadastrarClienteOutputPort {
    void presentSuccess(ClienteDomain cliente);

    void presentError(String message);

    boolean hasError();
}
