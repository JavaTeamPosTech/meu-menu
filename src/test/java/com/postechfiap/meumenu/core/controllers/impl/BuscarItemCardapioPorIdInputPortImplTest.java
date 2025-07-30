package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.BuscarItemCardapioPorIdUseCase; // Importar
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
class BuscarItemCardapioPorIdInputPortImplTest {

    @Mock
    private BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;

    @InjectMocks
    private BuscarItemCardapioPorIdInputPortImpl buscarItemCardapioPorIdInputPort;

    private UUID restauranteId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve delegar a execução para o BuscarItemCardapioPorIdUseCase corretamente")
    void shouldDelegateExecutionToBuscarItemCardapioPorIdUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> buscarItemCardapioPorIdInputPort.execute(restauranteId, itemId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(buscarItemCardapioPorIdUseCase, times(1)).execute(
                eq(restauranteId),
                eq(itemId)
        );
    }
}