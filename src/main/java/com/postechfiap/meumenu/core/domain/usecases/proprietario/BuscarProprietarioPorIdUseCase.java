package com.postechfiap.meumenu.core.domain.usecases.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

import java.util.UUID;

public interface BuscarProprietarioPorIdUseCase {
    ProprietarioDomain execute(UUID id);
}