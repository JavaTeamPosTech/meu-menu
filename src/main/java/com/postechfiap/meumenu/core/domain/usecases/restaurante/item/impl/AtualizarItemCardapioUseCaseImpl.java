package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtualizarItemCardapioUseCaseImpl implements AtualizarItemCardapioUseCase {

    private final RestauranteGateway restauranteGateway;
    private final AtualizarItemCardapioOutputPort atualizarItemCardapioOutputPort;

    @Override
    public ItemCardapioDomain execute(UUID restauranteId, UUID itemId, ItemCardapioInputModel inputModel, UUID proprietarioLogadoId) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(restauranteId);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado para atualizar item.");
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
        ItemCardapioDomain itemExistente = itemOptional.get();

        itemExistente.setNome(inputModel.getNome());
        itemExistente.setDescricao(inputModel.getDescricao());
        itemExistente.setPreco(inputModel.getPreco());
        itemExistente.setDisponivelApenasNoRestaurante(inputModel.getDisponivelApenasNoRestaurante());
        itemExistente.setUrlFoto(inputModel.getUrlFoto());

        ItemCardapioDomain itemAtualizado = restauranteGateway.atualizarItemCardapio(itemExistente);

        atualizarItemCardapioOutputPort.presentSuccess(itemAtualizado);

        return itemAtualizado;
    }

}