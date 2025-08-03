package com.postechfiap.meumenu.core.domain.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

public interface BuscarProprietarioOutputPort {
    void presentSuccess(ProprietarioDomain proprietario);

    void presentNoContent(String s);
}