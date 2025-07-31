package com.postechfiap.meumenu.core.domain.usecases.admin;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;

public interface CadastrarAdminUseCase {
    AdminDomain execute(CadastrarAdminInputModel input);
}