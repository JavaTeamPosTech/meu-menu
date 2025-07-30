package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.usuario.AlterarSenhaUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaInputPortImplTest {

    @Mock
    private AlterarSenhaUseCase alterarSenhaUseCase;

    @InjectMocks
    private AlterarSenhaInputPortImpl alterarSenhaInputPort;

    private AlterarSenhaInputModel inputModel;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        inputModel = new AlterarSenhaInputModel(usuarioId, "senhaAntiga123", "novaSenha456");
    }

    @Test
    @DisplayName("Deve delegar a execução para o AlterarSenhaUseCase corretamente")
    void shouldDelegateExecutionToAlterarSenhaUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> alterarSenhaInputPort.execute(inputModel));

        verify(alterarSenhaUseCase, times(1)).execute(
                any(AlterarSenhaInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis no InputModel
        );
    }
}