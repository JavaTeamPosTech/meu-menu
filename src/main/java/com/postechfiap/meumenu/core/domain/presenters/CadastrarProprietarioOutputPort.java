package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

public interface CadastrarProprietarioOutputPort {
    void presentSuccess(ProprietarioDomain proprietario);
    void presentError(String message);
}