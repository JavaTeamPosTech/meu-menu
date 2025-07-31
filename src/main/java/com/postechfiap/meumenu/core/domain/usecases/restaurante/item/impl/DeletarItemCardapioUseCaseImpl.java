package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.DeletarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeletarItemCardapioUseCaseImpl implements DeletarItemCardapioUseCase {

    private final RestauranteGateway restauranteGateway;
    private final DeletarItemCardapioOutputPort deletarItemCardapioOutputPort;

    @Override
    public void execute(UUID restauranteId, UUID itemId, UUID proprietarioLogadoId) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(restauranteId);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado para deletar item.");
        }
        RestauranteDomain restauranteExistente = restauranteOptional.get();

        if (!restauranteExistente.getProprietario().getId().equals(proprietarioLogadoId)) {
            throw new BusinessException("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.");
        }

        Optional<ItemCardapioDomain> itemOptional = restauranteExistente.getItensCardapio().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (itemOptional.isEmpty()) {
            throw new ResourceNotFoundException("Item do cardápio com ID " + itemId + " não encontrado no restaurante ID " + restauranteId + ".");
        }
        ItemCardapioDomain itemParaRemover = itemOptional.get();

        boolean removido = restauranteExistente.getItensCardapio().remove(itemParaRemover);
        if (!removido) {
            throw new BusinessException("Falha interna ao remover item da coleção do restaurante.");
        }

        restauranteGateway.atualizarRestaurante(restauranteExistente);
        deletarItemCardapioOutputPort.presentSuccess("Item do cardápio com ID " + itemId + " excluído com sucesso do restaurante ID " + restauranteId + ".");
    }
}