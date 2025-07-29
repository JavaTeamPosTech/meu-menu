package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.AdicionarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
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
public class AdicionarItemCardapioUseCaseImpl implements AdicionarItemCardapioUseCase {

    private final RestauranteGateway restauranteGateway;
    private final AdicionarItemCardapioOutputPort adicionarItemCardapioOutputPort;

    @Override
    public ItemCardapioDomain execute(UUID restauranteId, ItemCardapioInputModel input) {
        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(restauranteId);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado para adicionar item.");
        }
        RestauranteDomain restauranteExistente = restauranteOptional.get();


        ItemCardapioDomain novoItem = new ItemCardapioDomain(
                input.getNome(),
                input.getDescricao(),
                input.getPreco(),
                input.getDisponivelApenasNoRestaurante(),
                input.getUrlFoto()
        );

        novoItem.setRestaurante(restauranteExistente);
        ItemCardapioDomain itemSalvo = restauranteGateway.adicionarItemCardapio(restauranteId, novoItem);

        adicionarItemCardapioOutputPort.presentSuccess(itemSalvo);

        return itemSalvo;
    }
}