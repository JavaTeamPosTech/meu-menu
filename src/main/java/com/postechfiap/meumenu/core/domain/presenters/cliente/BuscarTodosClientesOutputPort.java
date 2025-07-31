package com.postechfiap.meumenu.core.domain.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

import java.util.List;

public interface BuscarTodosClientesOutputPort {
    void presentSuccess(List<ClienteDomain> clientes);
    void presentNoContent(String message);
}
