package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;

public interface CadastrarAdminOutputPort {
    void presentSuccess(AdminDomain admin);
    void presentError(String message);
}