package com.postechfiap.meumenu.core.controllers.admin;

import com.postechfiap.meumenu.core.dtos.admin.CadastrarAdminInputModel;

public interface CadastrarAdminInputPort {
    void execute(CadastrarAdminInputModel input);
}