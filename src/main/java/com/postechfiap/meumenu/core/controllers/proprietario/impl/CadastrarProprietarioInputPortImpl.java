package com.postechfiap.meumenu.core.controllers.proprietario.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.CadastrarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarProprietarioInputPortImpl implements CadastrarProprietarioInputPort {

    private final CadastrarProprietarioUseCase cadastrarProprietarioUseCase;

    private final CadastrarProprietarioOutputPort cadastrarProprietarioOutputPort;

    @Override
    public void execute(CadastrarProprietarioInputModel input) {
        try {
            ProprietarioDomain proprietarioSalvo = cadastrarProprietarioUseCase.execute(input);
            cadastrarProprietarioOutputPort.presentSuccess(proprietarioSalvo);
        } catch (Exception e) {
            cadastrarProprietarioOutputPort.presentError(e.getMessage());
        }

    }
}