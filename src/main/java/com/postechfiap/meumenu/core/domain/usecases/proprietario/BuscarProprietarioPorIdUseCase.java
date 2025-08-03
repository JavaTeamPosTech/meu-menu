package com.postechfiap.meumenu.core.domain.usecases.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

import java.util.Optional;
import java.util.UUID;

public interface BuscarProprietarioPorIdUseCase {
    Optional<ProprietarioDomain> execute(UUID id);
}