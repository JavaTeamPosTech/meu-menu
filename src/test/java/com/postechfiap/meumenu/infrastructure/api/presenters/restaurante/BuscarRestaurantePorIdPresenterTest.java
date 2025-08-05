package com.postechfiap.meumenu.infrastructure.api.presenters.restaurante;

import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuscarRestaurantePorIdPresenterTest {

    private BuscarRestaurantePorIdPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new BuscarRestaurantePorIdPresenter();
    }

    @Test
    void testPresentSuccess_ComDadosValidos() {
        RestauranteDomain restaurante = new RestauranteDomain(
                UUID.randomUUID(),
                "12345678000199",
                "Razão Social Teste",
                "Nome Fantasia Teste",
                "123456789",
                "11987654321",
                new ProprietarioDomain(),
                new EnderecoRestauranteDomain(
                        UUID.randomUUID(), "SP", "São Paulo", "Centro", "Rua Teste", 123, "Apto 1", "01001000", new RestauranteDomain()
                ),
                List.of(new TipoCozinhaDomain(UUID.randomUUID(), "Italiana")),
                List.of(new HorarioFuncionamentoDomain(UUID.randomUUID(), LocalTime.of(9, 0), LocalTime.of(18, 0), DiaSemanaEnum.SEGUNDA_FEIRA, new RestauranteDomain())),
                List.of(new ItemCardapioDomain(UUID.randomUUID(), "Pizza", "Pizza de Calabresa", BigDecimal.valueOf(50.0), true, "urlFoto", new RestauranteDomain()))
        );

        presenter.presentSuccess(restaurante);

        RestauranteResponseDTO viewModel = presenter.getViewModel();
        assertNotNull(viewModel);
        assertEquals(restaurante.getId(), viewModel.id());
        assertEquals(restaurante.getCnpj(), viewModel.cnpj());
        assertEquals(restaurante.getRazaoSocial(), viewModel.razaoSocial());
        assertEquals(restaurante.getNomeFantasia(), viewModel.nomeFantasia());
        assertEquals(restaurante.getTelefoneComercial(), viewModel.telefoneComercial());
        assertNotNull(viewModel.endereco());
        assertNotNull(viewModel.tiposCozinha());
        assertNotNull(viewModel.horariosFuncionamento());
        assertNotNull(viewModel.itensCardapio());
    }
}