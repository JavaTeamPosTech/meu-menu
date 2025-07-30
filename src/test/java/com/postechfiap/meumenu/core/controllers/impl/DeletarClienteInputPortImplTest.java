package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.DeletarClienteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.cliente.DeletarClienteUseCase;
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
class DeletarClienteInputPortImplTest {

    @Mock
    private DeletarClienteUseCase deletarClienteUseCase;

    @InjectMocks
    private DeletarClienteInputPortImpl deletarClienteInputPort;

    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o DeletarClienteUseCase corretamente")
    void shouldDelegateExecutionToDeletarClienteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> deletarClienteInputPort.execute(clienteId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(deletarClienteUseCase, times(1)).execute(eq(clienteId));
    }
}