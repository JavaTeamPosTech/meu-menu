package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.DeletarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarProprietarioInputPortImpl implements DeletarProprietarioInputPort {

    private final DeletarProprietarioUseCase deletarProprietarioUseCase;

    private final DeletarProprietarioOutputPort deletarProprietarioOutputPort;

    @Override
    public void execute(UUID id) {
        try {
            deletarProprietarioUseCase.execute(id);
            deletarProprietarioOutputPort.presentSuccess("Proprietário com ID " + id + " excluído com sucesso.");
        } catch (Exception e) {
            deletarProprietarioOutputPort.presentError(e.getMessage());
        }


    }
}