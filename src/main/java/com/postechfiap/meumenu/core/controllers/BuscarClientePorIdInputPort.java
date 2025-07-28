package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

import java.util.Optional;
import java.util.UUID;

public interface BuscarClientePorIdInputPort {
    Optional<ClienteDomain> execute(UUID id);
}
