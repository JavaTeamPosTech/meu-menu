package com.postechfiap.meumenu.core.domain.usecases.admin.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosProprietariosAdminUseCaseImpl implements BuscarTodosProprietariosAdminUseCase {

    private final ProprietarioGateway proprietarioGateway;

    @Override
    public List<ProprietarioDomain> execute() {
        return proprietarioGateway.buscarTodosProprietarios();
    }
}