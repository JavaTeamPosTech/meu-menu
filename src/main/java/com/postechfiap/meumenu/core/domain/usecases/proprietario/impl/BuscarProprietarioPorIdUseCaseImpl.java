package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BuscarProprietarioPorIdUseCaseImpl implements BuscarProprietarioPorIdUseCase {

    private final ProprietarioGateway proprietarioGateway;


    @Override
    public Optional<ProprietarioDomain> execute(UUID id) {
        return proprietarioGateway.buscarProprietarioPorId(id);
    }
}