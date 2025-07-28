package com.postechfiap.meumenu.core.domain.usecases.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

import java.util.Optional;
import java.util.UUID;

public interface BuscarClientePorIdUseCase {
    Optional<ClienteDomain> execute(UUID id);
}
