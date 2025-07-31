package com.postechfiap.meumenu.core.domain.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

public interface AtualizarProprietarioOutputPort {
    void presentSuccess(ProprietarioDomain proprietario);
}