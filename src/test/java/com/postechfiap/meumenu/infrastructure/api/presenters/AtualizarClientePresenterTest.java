package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.AtualizarClientePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AtualizarClientePresenterTest {

    @InjectMocks
    private AtualizarClientePresenter atualizarClientePresenter;

    private ClienteDomain clienteDomain;
    private UUID clienteId;
    private LocalDateTime dataCriacao;
    private EnderecoDomain enderecoDomain;
    private UUID enderecoId;
    private UsuarioDomain usuarioDomainParaEndereco;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        dataCriacao = LocalDateTime.now().minusHours(1);
        enderecoId = UUID.randomUUID();
        UUID usuarioIdParaEndereco = UUID.randomUUID();

        usuarioDomainParaEndereco = mock(UsuarioDomain.class);
//        when(usuarioDomainParaEndereco.getId()).thenReturn(usuarioIdParaEndereco);

        enderecoDomain = new EnderecoDomain(
                enderecoId, "SP", "Sao Paulo", "Centro", "Rua Teste", 123, "Apto 1", "01000-000",
                usuarioDomainParaEndereco // Associando o mock
        );
//        when(enderecoDomain.getUsuario()).thenReturn(usuarioDomainParaEndereco);


        clienteDomain = new ClienteDomain(
                clienteId,
                "12345678900", // CPF
                LocalDate.of(1990, 1, 1),
                GeneroEnum.MASCULINO,
                "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX,
                true, false, 0, 0, null,
                "Cliente Teste", "teste@email.com", "testelogin", "senha_hash",
                dataCriacao, LocalDateTime.now(), Collections.singletonList(enderecoDomain)
        );
//        when(clienteDomain.getEnderecos()).thenReturn(Collections.singletonList(enderecoDomain));
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente com todos os dados")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        atualizarClientePresenter.presentSuccess(clienteDomain);

        ClienteResponseDTO viewModel = atualizarClientePresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(clienteId, viewModel.id());
        assertEquals("Cliente Teste", viewModel.nome());
        assertEquals("12345678900", viewModel.cpf());
        assertEquals(LocalDate.of(1990, 1, 1), viewModel.dataNascimento());
        assertEquals("teste@email.com", viewModel.email());
        assertEquals("testelogin", viewModel.login());
        assertEquals("11987654321", viewModel.telefone());
        assertEquals(GeneroEnum.MASCULINO, viewModel.genero());
        assertEquals(new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)), viewModel.preferenciasAlimentares());
        assertEquals(new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)), viewModel.alergias());
        assertEquals(MetodoPagamentoEnum.PIX, viewModel.metodoPagamentoPreferido());
        assertEquals(true, viewModel.notificacoesAtivas());
        assertEquals(dataCriacao, viewModel.dataCriacao());

        assertNotNull(viewModel.enderecos());
        assertFalse(viewModel.enderecos().isEmpty());
        assertEquals(1, viewModel.enderecos().size());
        assertEquals(enderecoId, viewModel.enderecos().get(0).id());
        assertEquals("SP", viewModel.enderecos().get(0).estado());
        assertEquals("01000-000", viewModel.enderecos().get(0).cep());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        atualizarClientePresenter.presentSuccess(clienteDomain);
        ClienteResponseDTO currentViewModel = atualizarClientePresenter.getViewModel();

        assertNotNull(currentViewModel);
        assertEquals(clienteId, currentViewModel.id());
    }
}