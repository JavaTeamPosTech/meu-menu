package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase; // Importar
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BuscarProprietarioPorIdInputPortImpl implements BuscarProprietarioPorIdInputPort {

    private final BuscarProprietarioPorIdUseCase buscarProprietarioPorIdUseCase;

    @Override
    public void execute(UUID id) {
        buscarProprietarioPorIdUseCase.execute(id);
    }
}