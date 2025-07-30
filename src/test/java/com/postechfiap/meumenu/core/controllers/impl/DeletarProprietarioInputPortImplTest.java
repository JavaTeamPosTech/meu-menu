package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeletarProprietarioInputPortImplTest {

    @Mock
    private DeletarProprietarioUseCase deletarProprietarioUseCase;

    @InjectMocks
    private DeletarProprietarioInputPortImpl deletarProprietarioInputPort;

    private UUID proprietarioId;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o DeletarProprietarioUseCase corretamente")
    void shouldDelegateExecutionToDeletarProprietarioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> deletarProprietarioInputPort.execute(proprietarioId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(deletarProprietarioUseCase, times(1)).execute(eq(proprietarioId));
    }
}