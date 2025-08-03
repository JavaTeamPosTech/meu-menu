package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeletarProprietarioUseCaseImpl implements DeletarProprietarioUseCase {

    private final ProprietarioGateway proprietarioGateway;


    @Override
    public void execute(UUID id) {
        Optional<ProprietarioDomain> proprietarioOptional = proprietarioGateway.buscarProprietarioPorId(id);

        if (proprietarioOptional.isEmpty()) {
            throw new ResourceNotFoundException("Proprietário com ID " + id + " não encontrado para exclusão.");
        }

        proprietarioGateway.deletarProprietario(id);


    }
}