package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

public interface CadastrarClienteOutputPort {
    void presentSuccess(ClienteDomain cliente);
    void presentError(String message);
}
