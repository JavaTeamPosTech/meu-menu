package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.AtualizarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.AtualizarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarProprietarioInputPortImpl implements AtualizarProprietarioInputPort {

    private final AtualizarProprietarioUseCase atualizarProprietarioUseCase;

    private final AtualizarProprietarioOutputPort atualizarProprietarioOutputPort;

    @Override
    public void execute(AtualizarProprietarioInputModel input) {
        try {
            ProprietarioDomain proprietarioAtualizado = atualizarProprietarioUseCase.execute(input);
            atualizarProprietarioOutputPort.presentSuccess(proprietarioAtualizado);
        } catch (Exception e) {
            atualizarProprietarioOutputPort.presentError(e.getMessage());
        }


    }
}