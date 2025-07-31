package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.AtualizarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarProprietarioInputPortImpl implements AtualizarProprietarioInputPort {

    private final AtualizarProprietarioUseCase atualizarProprietarioUseCase;

    @Override
    public void execute(AtualizarProprietarioInputModel input) {
        atualizarProprietarioUseCase.execute(input);
    }
}