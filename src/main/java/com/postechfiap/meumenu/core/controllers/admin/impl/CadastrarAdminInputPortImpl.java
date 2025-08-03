package com.postechfiap.meumenu.core.controllers.admin.impl;

import com.postechfiap.meumenu.core.controllers.admin.CadastrarAdminInputPort;
import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.domain.presenters.admin.CadastrarAdminOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.CadastrarAdminUseCase;
import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarAdminInputPortImpl implements CadastrarAdminInputPort {

    private final CadastrarAdminUseCase cadastrarAdminUseCase;

    private final CadastrarAdminOutputPort cadastrarAdminOutputPort;

    @Override
    public void execute(CadastrarAdminInputModel input) {
        try {
            AdminDomain admin = cadastrarAdminUseCase.execute(input);
            cadastrarAdminOutputPort.presentSuccess(admin);
        } catch (BusinessException e) {
            cadastrarAdminOutputPort.presentError(e.getMessage());
        }

    }
}
