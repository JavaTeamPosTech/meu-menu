package com.postechfiap.meumenu.core.domain.usecases.admin;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

import java.util.List;

public interface BuscarTodosProprietariosAdminUseCase {
    List<ProprietarioDomain> execute();
}
