package com.postechfiap.meumenu.core.domain.usecases.admin.impl;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.domain.usecases.admin.CadastrarAdminUseCase;
import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.gateways.AdminGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CadastrarAdminUseCaseImpl implements CadastrarAdminUseCase {

    private final AdminGateway adminGateway;
    private final UsuarioGateway usuarioGateway;
    private final PasswordService passwordService;


    @Override
    public AdminDomain execute(CadastrarAdminInputModel input) {
        if (usuarioGateway.existsByLogin(input.getLogin())) {
            throw new BusinessException("Login já cadastrado.");
        }
        if (usuarioGateway.existsByEmail(input.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }

        String senhaCriptografada = passwordService.encryptPassword(input.getSenha());

        AdminDomain novoAdmin = new AdminDomain(
                input.getNome(),
                input.getEmail(),
                input.getLogin(),
                senhaCriptografada
        );

        return adminGateway.cadastrarAdmin(novoAdmin);
    }
}