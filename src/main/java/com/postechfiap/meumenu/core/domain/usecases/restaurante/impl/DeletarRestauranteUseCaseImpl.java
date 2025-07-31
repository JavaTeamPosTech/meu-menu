package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.DeletarRestauranteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.DeletarRestauranteUseCase;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeletarRestauranteUseCaseImpl implements DeletarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final DeletarRestauranteOutputPort deletarRestauranteOutputPort;

    @Override
    public void execute(UUID id, UUID proprietarioLogadoId) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(id);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + id + " não encontrado para exclusão.");
        }
        RestauranteDomain restauranteExistente = restauranteOptional.get();

        if (!restauranteExistente.getProprietario().getId().equals(proprietarioLogadoId)) {
            throw new BusinessException("Acesso negado. O restaurante com ID " + id + " não pertence ao proprietário logado.");
        }

        restauranteGateway.deletarRestaurante(id);
        deletarRestauranteOutputPort.presentSuccess("Restaurante com ID " + id + " excluído com sucesso.");
    }
}