package com.postechfiap.meumenu.core.domain.usecases.admin.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarTodosProprietariosOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosProprietariosAdminUseCaseImpl implements BuscarTodosProprietariosAdminUseCase {

    private final ProprietarioGateway proprietarioGateway;
    private final BuscarTodosProprietariosOutputPort buscarTodosProprietariosOutputPort;

    @Override
    public List<ProprietarioDomain> execute() {
        List<ProprietarioDomain> proprietarios = proprietarioGateway.buscarTodosProprietarios();

        if (proprietarios.isEmpty()) {
            buscarTodosProprietariosOutputPort.presentNoContent("Nenhum proprietário encontrado para o administrador.");
            return Collections.emptyList();
        }

        buscarTodosProprietariosOutputPort.presentSuccess(proprietarios);
        return proprietarios;
    }
}