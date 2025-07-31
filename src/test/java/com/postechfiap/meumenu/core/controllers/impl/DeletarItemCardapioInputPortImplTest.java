package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.DeletarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
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
class DeletarItemCardapioInputPortImplTest {

    @Mock
    private DeletarItemCardapioUseCase deletarItemCardapioUseCase;

    @InjectMocks
    private DeletarItemCardapioInputPortImpl deletarItemCardapioInputPort;

    private UUID restauranteId;
    private UUID itemId;
    private UUID proprietarioLogadoId;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        proprietarioLogadoId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o DeletarItemCardapioUseCase corretamente")
    void shouldDelegateExecutionToDeletarItemCardapioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> deletarItemCardapioInputPort.execute(restauranteId, itemId, proprietarioLogadoId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(deletarItemCardapioUseCase, times(1)).execute(
                eq(restauranteId),
                eq(itemId),
                eq(proprietarioLogadoId)
        );
    }
}