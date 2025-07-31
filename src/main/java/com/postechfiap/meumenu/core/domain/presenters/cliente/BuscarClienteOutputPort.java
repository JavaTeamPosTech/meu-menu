package com.postechfiap.meumenu.core.domain.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

public interface BuscarClienteOutputPort {
    void presentSuccess(ClienteDomain cliente);
}
