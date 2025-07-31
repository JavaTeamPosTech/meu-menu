package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BuscarTodosProprietariosAdminAdminInputPortImpl implements BuscarTodosProprietariosAdminInputPort {

    private final BuscarTodosProprietariosAdminUseCase buscarTodosProprietariosAdminUseCase;

    @Override
    public void execute() {
        buscarTodosProprietariosAdminUseCase.execute();
    }
}