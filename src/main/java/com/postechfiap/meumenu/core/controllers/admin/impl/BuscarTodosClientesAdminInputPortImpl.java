package com.postechfiap.meumenu.core.controllers.admin.impl;

import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosClientesAdminInputPortImpl implements BuscarTodosClientesAdminInputPort {

    private final BuscarTodosClientesAdminUseCase buscarTodosClientesAdminUseCase;

    private final BuscarTodosClientesOutputPort buscarTodosClientesOutputPort;

    @Override
    public void execute() {
        List<ClienteDomain> clienteDomains = buscarTodosClientesAdminUseCase.execute();
        if (clienteDomains.isEmpty()) {
            buscarTodosClientesOutputPort.presentNoContent("Nenhum cliente encontrado para o administrador.");
        } else{
            buscarTodosClientesOutputPort.presentSuccess(clienteDomains);
        }

    }
}