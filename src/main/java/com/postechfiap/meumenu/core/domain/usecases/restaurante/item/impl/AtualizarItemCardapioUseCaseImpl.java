package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarItemCardapioOutputPort; // NOVO: OutputPort
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
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
    public ItemCardapioDomain execute(UUID restauranteId, UUID itemId, ItemCardapioDomain itemCardapioAtualizado, UUID proprietarioLogadoId) {
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

        itemExistente.setNome(itemCardapioAtualizado.getNome());
        itemExistente.setDescricao(itemCardapioAtualizado.getDescricao());
        itemExistente.setPreco(itemCardapioAtualizado.getPreco());
        itemExistente.setDisponivelApenasNoRestaurante(itemCardapioAtualizado.getDisponivelApenasNoRestaurante());
        itemExistente.setUrlFoto(itemCardapioAtualizado.getUrlFoto());

        RestauranteDomain restauranteAtualizado = restauranteGateway.atualizarRestaurante(restauranteExistente);

        ItemCardapioDomain itemAtualizadoRetornado = restauranteAtualizado.getItensCardapio().stream()
                .filter(item -> item.getId().equals(itemExistente.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Falha ao recuperar o item cardápio atualizado."));

        atualizarItemCardapioOutputPort.presentSuccess(itemAtualizadoRetornado);

        return itemAtualizadoRetornado;
    }
}