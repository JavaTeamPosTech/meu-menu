package com.postechfiap.meumenu.core.domain.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

public interface CadastrarProprietarioOutputPort {
    void presentSuccess(ProprietarioDomain proprietario);

    void presentError(String message);

    boolean hasError();
}