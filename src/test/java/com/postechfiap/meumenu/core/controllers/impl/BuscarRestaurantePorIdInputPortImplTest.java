package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.impl.BuscarRestaurantePorIdInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.BuscarRestaurantePorIdUseCase;
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
class BuscarRestaurantePorIdInputPortImplTest {

    @Mock
    private BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @InjectMocks
    private BuscarRestaurantePorIdInputPortImpl buscarRestaurantePorIdInputPort;

    private UUID restauranteId;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o BuscarRestaurantePorIdUseCase corretamente")
    void shouldDelegateExecutionToBuscarRestaurantePorIdUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> buscarRestaurantePorIdInputPort.execute(restauranteId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(buscarRestaurantePorIdUseCase, times(1)).execute(eq(restauranteId));
    }
}