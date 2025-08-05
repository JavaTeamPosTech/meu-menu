package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuscarProprietarioPorIdPresenterTest {

    private BuscarProprietarioPorIdPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new BuscarProprietarioPorIdPresenter();
    }

    @Test
    void testMapEnderecoDomainToResponseDTO() {
        EnderecoDomain enderecoDomain = new EnderecoDomain(
                UUID.randomUUID(),
                "SP",
                "São Paulo",
                "Centro",
                "Rua Teste",
                123,
                "Apto 1",
                "01001000",
                new ProprietarioDomain()
        );


        EnderecoResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapEnderecoDomainToResponseDTO", enderecoDomain
        );

        assertNotNull(responseDTO);
        assertEquals(enderecoDomain.getId(), responseDTO.id());
        assertEquals(enderecoDomain.getEstado(), responseDTO.estado());
        assertEquals(enderecoDomain.getCidade(), responseDTO.cidade());
        assertEquals(enderecoDomain.getBairro(), responseDTO.bairro());
    }

    @Test
    void testMapRestauranteDomainToResponseDTO() {
        RestauranteDomain restauranteDomain = new RestauranteDomain(
                UUID.randomUUID(),
                "12345678000199",
                "Razão Social Teste",
                "Nome Fantasia Teste",
                "123456789",
                "11987654321",
                null,
                null,
                List.of(new TipoCozinhaDomain(UUID.randomUUID(), "Italiana")),
                List.of(new HorarioFuncionamentoDomain(UUID.randomUUID(), LocalTime.of(9, 0), LocalTime.of(18, 0), DiaSemanaEnum.SEGUNDA_FEIRA, new RestauranteDomain())),
                null
        );

        RestauranteResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapRestauranteDomainToResponseDTO", restauranteDomain
        );

        assertNotNull(responseDTO);
        assertEquals(restauranteDomain.getId(), responseDTO.id());
        assertEquals(restauranteDomain.getCnpj(), responseDTO.cnpj());
        assertEquals(restauranteDomain.getRazaoSocial(), responseDTO.razaoSocial());
        assertEquals(restauranteDomain.getNomeFantasia(), responseDTO.nomeFantasia());
    }

    @Test
    void testMapTipoCozinhaDomainToResponseDTO() {
        TipoCozinhaDomain tipoCozinhaDomain = new TipoCozinhaDomain(
                UUID.randomUUID(),
                "Italiana"
        );

        TipoCozinhaResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapTipoCozinhaDomainToResponseDTO", tipoCozinhaDomain
        );

        assertNotNull(responseDTO);
        assertEquals(tipoCozinhaDomain.getId(), responseDTO.id());
        assertEquals(tipoCozinhaDomain.getNome(), responseDTO.nome());
    }

    @Test
    void testMapItemCardapioDomainToResponseDTO() {
        ItemCardapioDomain itemCardapioDomain = new ItemCardapioDomain(
                UUID.randomUUID(),
                "Pizza",
                "Pizza de Calabresa",
                BigDecimal.valueOf(50.0),
                true,
                "urlFoto",
                new RestauranteDomain()
        );

        ItemCardapioResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapItemCardapioDomainToResponseDTO", itemCardapioDomain
        );

        assertNotNull(responseDTO);
        assertEquals(itemCardapioDomain.getId(), responseDTO.id());
        assertEquals(itemCardapioDomain.getNome(), responseDTO.nome());
        assertEquals(itemCardapioDomain.getDescricao(), responseDTO.descricao());
        assertEquals(itemCardapioDomain.getPreco(), responseDTO.preco());
        assertEquals(itemCardapioDomain.getDisponivelApenasNoRestaurante(), responseDTO.disponivelApenasNoRestaurante());
        assertEquals(itemCardapioDomain.getUrlFoto(), responseDTO.urlFoto());
    }
}