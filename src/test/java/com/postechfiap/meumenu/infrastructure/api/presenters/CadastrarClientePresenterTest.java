package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.dtos.response.CadastrarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.CadastrarClientePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Necessário se usar @Mock/@InjectMocks
import org.mockito.InjectMocks; // Para injetar o SUT
import org.mockito.junit.jupiter.MockitoExtension; // Para inicializar mocks

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Usar se precisar de mocks, embora aqui não seja estritamente necessário
class CadastrarClientePresenterTest {

    @InjectMocks
    private CadastrarClientePresenter cadastrarClientePresenter;

    private ClienteDomain clienteDomain;
    private UUID clienteId;
    private LocalDateTime dataCriacao;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        dataCriacao = LocalDateTime.now().minusHours(1);

        // ClienteDomain de exemplo para o Presenter
        clienteDomain = new ClienteDomain(
                clienteId,
                "12345678900",
                LocalDate.of(1990, 1, 1),
                GeneroEnum.MASCULINO,
                "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX,
                true, false, 0, 0, null,
                "Cliente Teste", "teste@email.com", "testelogin", "senha_hash",
                dataCriacao, LocalDateTime.now(), Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        // Executa o método presentSuccess
        cadastrarClientePresenter.presentSuccess(clienteDomain);

        // Obtém o ViewModel
        CadastrarClienteResponseDTO viewModel = cadastrarClientePresenter.getViewModel();

        // Verifica os campos do ViewModel
        assertNotNull(viewModel);
        assertEquals(clienteId, viewModel.getId());
        assertEquals("Cliente Teste", viewModel.getNome());
        assertEquals("teste@email.com", viewModel.getEmail());
        assertEquals("testelogin", viewModel.getLogin());
        assertEquals(dataCriacao, viewModel.getDataCriacao());
        assertEquals(" Cliente cadastrado com sucesso!", viewModel.getMessage());
        assertEquals("SUCCESS", viewModel.getStatus());
    }

    @Test
    @DisplayName("Deve apresentar erro e popular o ViewModel corretamente")
    void shouldPresentErrorAndPopulateViewModelCorrectly() {
        String errorMessage = "Login já cadastrado.";

        // Executa o método presentError
        cadastrarClientePresenter.presentError(errorMessage);

        // Obtém o ViewModel
        CadastrarClienteResponseDTO viewModel = cadastrarClientePresenter.getViewModel();

        // Verifica os campos do ViewModel para o cenário de erro
        assertNotNull(viewModel);
        assertNull(viewModel.getId()); // ID deve ser nulo em caso de erro
        assertNull(viewModel.getNome()); // Outros campos de dados nulos
        assertEquals(errorMessage, viewModel.getMessage());
        assertEquals("FAIL", viewModel.getStatus());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        // Primeiro, popula o ViewModel com sucesso
        cadastrarClientePresenter.presentSuccess(clienteDomain);
        CadastrarClienteResponseDTO successViewModel = cadastrarClientePresenter.getViewModel();

        // Verifica se é o mesmo objeto
        assertNotNull(successViewModel);

        // Agora, popula o ViewModel com erro
        cadastrarClientePresenter.presentError("Erro qualquer.");
        CadastrarClienteResponseDTO errorViewModel = cadastrarClientePresenter.getViewModel();

        // Verifica se o ViewModel mudou
        assertNotNull(errorViewModel);
        assertNotEquals(successViewModel, errorViewModel); // Devem ser objetos diferentes
        assertEquals("Erro qualquer.", errorViewModel.getMessage());
    }
}