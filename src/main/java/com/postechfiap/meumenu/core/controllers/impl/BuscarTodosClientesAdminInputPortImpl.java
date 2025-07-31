package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarTodosClientesAdminInputPortImpl implements BuscarTodosClientesAdminInputPort {

    private final BuscarTodosClientesAdminUseCase buscarTodosClientesAdminUseCase;

    @Override
    public void execute() {
        buscarTodosClientesAdminUseCase.execute();
    }
}