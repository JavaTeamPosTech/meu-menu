package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.usuario.impl.LoginInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginInputPortImplTest {

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private LoginInputPortImpl loginInputPort;

    private LoginInputModel inputModel;

    @BeforeEach
    void setUp() {
        inputModel = new LoginInputModel("testuser", "testpassword");
    }

    @Test
    @DisplayName("Deve delegar a execução para o LoginUseCase corretamente")
    void shouldDelegateExecutionToLoginUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> loginInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(loginUseCase, times(1)).execute(any(LoginInputModel.class)); // Pode usar eq(inputModel) se equals/hashCode forem confiáveis
    }
}