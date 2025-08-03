package com.postechfiap.meumenu.core.controllers.admin.impl;

import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.BuscarTodosProprietariosOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuscarTodosProprietariosAdminAdminInputPortImpl implements BuscarTodosProprietariosAdminInputPort {

    private final BuscarTodosProprietariosAdminUseCase buscarTodosProprietariosAdminUseCase;

    private final BuscarTodosProprietariosOutputPort buscarTodosProprietariosOutputPort;

    @Override
    public void execute() {
        List<ProprietarioDomain> proprietarioDomains = buscarTodosProprietariosAdminUseCase.execute();

        if (proprietarioDomains.isEmpty()) {
            buscarTodosProprietariosOutputPort.presentNoContent("Nenhum proprietário encontrado para o administrador.");
        } else {
            buscarTodosProprietariosOutputPort.presentSuccess(proprietarioDomains);
        }

    }
}