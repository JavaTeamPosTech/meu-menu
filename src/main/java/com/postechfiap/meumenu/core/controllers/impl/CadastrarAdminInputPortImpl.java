package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.CadastrarAdminInputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.CadastrarAdminUseCase;
import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarAdminInputPortImpl implements CadastrarAdminInputPort {

    private final CadastrarAdminUseCase cadastrarAdminUseCase;

    @Override
    public void execute(CadastrarAdminInputModel input) {
        cadastrarAdminUseCase.execute(input);
    }
}
