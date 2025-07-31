package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.impl.DeletarRestauranteInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.DeletarRestauranteUseCase;
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
class DeletarRestauranteInputPortImplTest {

    @Mock
    private DeletarRestauranteUseCase deletarRestauranteUseCase;

    @InjectMocks
    private DeletarRestauranteInputPortImpl deletarRestauranteInputPort;

    private UUID restauranteId;
    private UUID proprietarioLogadoId;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        proprietarioLogadoId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o DeletarRestauranteUseCase corretamente")
    void shouldDelegateExecutionToDeletarRestauranteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> deletarRestauranteInputPort.execute(restauranteId, proprietarioLogadoId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(deletarRestauranteUseCase, times(1)).execute(
                eq(restauranteId),
                eq(proprietarioLogadoId)
        );
    }
}