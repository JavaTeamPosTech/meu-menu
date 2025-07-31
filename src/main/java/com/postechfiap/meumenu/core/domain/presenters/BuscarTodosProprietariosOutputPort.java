package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

import java.util.List;

public interface BuscarTodosProprietariosOutputPort {
    void presentSuccess(List<ProprietarioDomain> proprietarios);
    void presentNoContent(String message);
}
