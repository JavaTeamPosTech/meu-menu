package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

public interface BuscarProprietarioOutputPort {
    void presentSuccess(ProprietarioDomain proprietario);
}