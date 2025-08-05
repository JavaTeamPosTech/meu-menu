package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuscarTodosProprietariosPresenterTest {

    private BuscarTodosProprietariosPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new BuscarTodosProprietariosPresenter();
    }

    @Test
    void testPresentSuccess() {
        ProprietarioDomain proprietario = new ProprietarioDomain(
                UUID.randomUUID(),
                "12345678900",
                "11987654321",
                StatusContaEnum.ATIVO,
                "Nome Proprietário",
                "email@teste.com",
                "login_teste",
                "hash_senha",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        presenter.presentSuccess(List.of(proprietario));

        assertNotNull(presenter.getViewModel());
        assertFalse(presenter.getViewModel().isEmpty());
        assertEquals(1, presenter.getViewModel().size());
        ProprietarioResponseDTO responseDTO = presenter.getViewModel().get(0);
        assertEquals(proprietario.getId(), responseDTO.id());
        assertEquals(proprietario.getNome(), responseDTO.nome());
        assertEquals(proprietario.getCpf(), responseDTO.cpf());
    }

    @Test
    void testPresentNoContent() {
        String noContentMessage = "Nenhum proprietário encontrado.";

        presenter.presentNoContent(noContentMessage);

        assertNotNull(presenter.getViewModel());
        assertTrue(presenter.getViewModel().isEmpty());
        assertTrue(presenter.isNoContent());
        assertEquals(noContentMessage, presenter.getNoContentMessage());
    }

    @Test
    void testMapProprietarioDomainToResponseDTO() {
        ProprietarioDomain proprietario = new ProprietarioDomain(
                UUID.randomUUID(),
                "12345678900",
                "11987654321",
                StatusContaEnum.ATIVO,
                "Nome Proprietário",
                "email@teste.com",
                "login_teste",
                "hash_senha",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        ProprietarioResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapProprietarioDomainToResponseDTO", proprietario
        );

        assertNotNull(responseDTO);
        assertEquals(proprietario.getId(), responseDTO.id());
        assertEquals(proprietario.getNome(), responseDTO.nome());
        assertEquals(proprietario.getCpf(), responseDTO.cpf());
        assertEquals(proprietario.getEmail(), responseDTO.email());
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

        RestauranteResponseDTO responseDTO = ReflectionTestUtils.invokeMethod(
                presenter, "mapRestauranteDomainToResponseDTO", restaurante
        );

        assertNotNull(responseDTO);
        assertEquals(restaurante.getId(), responseDTO.id());
        assertEquals(restaurante.getCnpj(), responseDTO.cnpj());
        assertEquals(restaurante.getRazaoSocial(), responseDTO.razaoSocial());
        assertEquals(restaurante.getNomeFantasia(), responseDTO.nomeFantasia());
    }


}