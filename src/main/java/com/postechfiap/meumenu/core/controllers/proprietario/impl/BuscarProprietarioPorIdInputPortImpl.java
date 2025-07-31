package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase; // Importar
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BuscarProprietarioPorIdInputPortImpl implements BuscarProprietarioPorIdInputPort {

    private final BuscarProprietarioPorIdUseCase buscarProprietarioPorIdUseCase;

    @Override
    public void execute(UUID id) {
        buscarProprietarioPorIdUseCase.execute(id);
    }
}