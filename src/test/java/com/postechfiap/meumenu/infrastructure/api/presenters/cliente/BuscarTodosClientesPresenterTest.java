package com.postechfiap.meumenu.infrastructure.api.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BuscarTodosClientesPresenterTest {

    private BuscarTodosClientesPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new BuscarTodosClientesPresenter();
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
ClienteDomain cliente = new ClienteDomain(
        UUID.randomUUID(),
        "12345678900", // CPF
        LocalDate.of(1990, 1, 1),
        GeneroEnum.MASCULINO,
        "11987654321",
        Collections.emptySet(),
        Collections.emptySet(),
        MetodoPagamentoEnum.PIX,
        true,
        false,
        0,
        0,
        LocalDateTime.now(),
        "Cliente Teste",
        "cliente@email.com",
        "login_cliente",
        "hashPassword",
        LocalDateTime.now(),
        LocalDateTime.now(),
        Collections.emptyList()
);

        presenter.presentSuccess(List.of(cliente));

        List<ClienteResponseDTO> viewModel = presenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(1, viewModel.size());
        assertEquals(cliente.getId(), viewModel.get(0).id());
        assertEquals(cliente.getNome(), viewModel.get(0).nome());
        assertEquals(cliente.getCpf(), viewModel.get(0).cpf());
    }

    @Test
    @DisplayName("Deve apresentar NoContent e popular o ViewModel corretamente")
    void shouldPresentNoContentAndPopulateViewModelCorrectly() {
        String noContentMessage = "Nenhum cliente encontrado";

        presenter.presentNoContent(noContentMessage);

        assertTrue(presenter.isNoContent());
        assertEquals(noContentMessage, presenter.getNoContentMessage());
        assertNotNull(presenter.getViewModel());
        assertTrue(presenter.getViewModel().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear EnderecoDomain para EnderecoResponseDTO corretamente")
    void shouldMapEnderecoDomainToResponseDTOCorrectly() {
        EnderecoDomain endereco = new EnderecoDomain(
                UUID.randomUUID(),
                "SP",
                "São Paulo",
                "Centro",
                "Rua A",
                123,
                "Apto 1",
                "01001-000",
                new UsuarioDomain()
        );
        ClienteDomain cliente = new ClienteDomain(
                UUID.randomUUID(),
                "12345678900", // CPF
                LocalDate.of(1990, 1, 1),
                GeneroEnum.MASCULINO,
                "11987654321",
                Collections.emptySet(),
                Collections.emptySet(),
                MetodoPagamentoEnum.PIX,
                true,
                false,
                0,
                0,
                LocalDateTime.now(),
                "Cliente Teste",
                "cliente@email.com",
                "login_cliente",
                "hashPassword",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.singletonList(endereco)
        );



        presenter.presentSuccess(List.of(cliente));

        List<ClienteResponseDTO> viewModel = presenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(1, viewModel.size());
        assertNotNull(viewModel.get(0).enderecos());
        assertEquals(1, viewModel.get(0).enderecos().size());
        assertEquals(endereco.getId(), viewModel.get(0).enderecos().get(0).id());
        assertEquals(endereco.getEstado(), viewModel.get(0).enderecos().get(0).estado());
    }
}