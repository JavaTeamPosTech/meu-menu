package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarProprietarioPorIdUseCaseImpl implements BuscarProprietarioPorIdUseCase {

    private final ProprietarioGateway proprietarioGateway;
    private final BuscarProprietarioOutputPort buscarProprietarioOutputPort;

    @Override
    public ProprietarioDomain execute(UUID id) {
        Optional<ProprietarioDomain> proprietarioOptional = proprietarioGateway.buscarProprietarioPorId(id);

        if (proprietarioOptional.isEmpty()) {
            throw new ResourceNotFoundException("Proprietário com ID " + id + " não encontrado.");
        }
        buscarProprietarioOutputPort.presentSuccess(proprietarioOptional.get());
        return proprietarioOptional.get();
    }
}