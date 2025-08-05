package com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ItemCardapioResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuscarItemCardapioPorIdPresenterTest {

    private BuscarItemCardapioPorIdPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new BuscarItemCardapioPorIdPresenter();
    }

    @Test
    void testPresentSuccess_ComDadosValidos() {

        ItemCardapioDomain item = new ItemCardapioDomain(
                UUID.randomUUID(),
                "Pizza Calabresa",
                "Pizza de Calabresa e Mussarela",
                BigDecimal.valueOf(50.0),
                true,
                "urlFoto",
                new RestauranteDomain()
        );

        presenter.presentSuccess(item);

        ItemCardapioResponseDTO viewModel = presenter.getViewModel();
        assertNotNull(viewModel);
        assertEquals(item.getId(), viewModel.id());
        assertEquals(item.getNome(), viewModel.nome());
        assertEquals(item.getDescricao(), viewModel.descricao());
        assertEquals(item.getPreco(), viewModel.preco());
        assertEquals(item.getDisponivelApenasNoRestaurante(), viewModel.disponivelApenasNoRestaurante());
        assertEquals(item.getUrlFoto(), viewModel.urlFoto());

    }
}