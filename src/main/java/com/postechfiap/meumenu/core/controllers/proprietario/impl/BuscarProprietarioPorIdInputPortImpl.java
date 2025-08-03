package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.BuscarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarProprietarioPorIdInputPortImpl implements BuscarProprietarioPorIdInputPort {

    private final BuscarProprietarioPorIdUseCase buscarProprietarioPorIdUseCase;

    private final BuscarProprietarioOutputPort buscarProprietarioOutputPort;

    @Override
    public void execute(UUID id) {
        Optional<ProprietarioDomain> proprietarioOptional = buscarProprietarioPorIdUseCase.execute(id);

        if (proprietarioOptional.isEmpty()) {
            buscarProprietarioOutputPort.presentNoContent("Proprietário com ID " + id + " não encontrado.");
        } else {
            buscarProprietarioOutputPort.presentSuccess(proprietarioOptional.get());
        }


    }
}