package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarProprietarioInputPortImpl implements DeletarProprietarioInputPort {

    private final DeletarProprietarioUseCase deletarProprietarioUseCase;

    @Override
    public void execute(UUID id) {
        deletarProprietarioUseCase.execute(id);
    }
}