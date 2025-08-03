package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.dtos.response.MensagemResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.usuario.AlterarSenhaPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Necessário se usar @Mock/@InjectMocks
import org.mockito.InjectMocks; // Para injetar o SUT
import org.mockito.junit.jupiter.MockitoExtension; // Para inicializar mocks

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Usar se precisar de mocks, embora aqui não seja estritamente necessário
class AlterarSenhaPresenterTest {

    @InjectMocks
    private AlterarSenhaPresenter alterarSenhaPresenter;

    @BeforeEach
    void setUp() {
        // Nada específico para configurar, pois os métodos são simples e usam String.
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        String successMessage = "Senha alterada com sucesso.";

        alterarSenhaPresenter.presentSuccess(successMessage);

        MensagemResponseDTO viewModel = alterarSenhaPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(successMessage, viewModel.message());
        assertEquals("SUCCESS", viewModel.status());
    }

    @Test
    @DisplayName("Deve apresentar erro e popular o ViewModel corretamente")
    void shouldPresentErrorAndPopulateViewModelCorrectly() {
        String errorMessage = "Senha antiga incorreta.";

        alterarSenhaPresenter.presentError(errorMessage);

        MensagemResponseDTO viewModel = alterarSenhaPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(errorMessage, viewModel.message());
        assertEquals("FAIL", viewModel.status());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        alterarSenhaPresenter.presentSuccess("Operação bem sucedida.");
        MensagemResponseDTO successViewModel = alterarSenhaPresenter.getViewModel();

        // Verifica se é o mesmo objeto (referência)
        assertNotNull(successViewModel);
        assertEquals("SUCCESS", successViewModel.status());

        // Agora, popula o ViewModel com erro
        alterarSenhaPresenter.presentError("Operação falhou.");
        MensagemResponseDTO errorViewModel = alterarSenhaPresenter.getViewModel();

        // Verifica se o ViewModel mudou
        assertNotNull(errorViewModel);
        // Note: como MensagemResponseDTO é um record, a comparação direta pode ser por valor.
        // Mas a referência do objeto viewModel dentro do presenter terá mudado.
        assertNotEquals(successViewModel, errorViewModel); // Devem ser objetos diferentes se a instância foi recriada
        assertEquals("FAIL", errorViewModel.status());
    }
}