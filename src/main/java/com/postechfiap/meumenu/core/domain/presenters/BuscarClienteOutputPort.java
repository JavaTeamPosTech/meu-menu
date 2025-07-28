package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

public interface BuscarClienteOutputPort {
    void presentSuccess(ClienteDomain cliente);
}
