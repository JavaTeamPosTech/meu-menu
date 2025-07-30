package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarTodosClientesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuscarTodosClientesInputPortImplTest {

    @Mock
    private BuscarTodosClientesUseCase buscarTodosClientesUseCase;

    @InjectMocks
    private BuscarTodosClientesInputPortImpl buscarTodosClientesInputPort;

    @BeforeEach
    void setUp() {
        // Nada específico para configurar, pois o método execute() não recebe parâmetros.
    }

    @Test
    @DisplayName("Deve delegar a execução para o BuscarTodosClientesUseCase corretamente")
    void shouldDelegateExecutionToBuscarTodosClientesUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> buscarTodosClientesInputPort.execute());

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez.
        verify(buscarTodosClientesUseCase, times(1)).execute();
    }
}