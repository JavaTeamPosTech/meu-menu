package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.AdicionarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdicionarItemCardapioInputPortImplTest {

    @Mock
    private AdicionarItemCardapioUseCase adicionarItemCardapioUseCase;

    @InjectMocks
    private AdicionarItemCardapioInputPortImpl adicionarItemCardapioInputPort;

    private UUID restauranteId;
    private ItemCardapioInputModel itemInputModel;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemInputModel = new ItemCardapioInputModel(
                "Hamburguer Teste", "Delicioso hamburguer artesanal", BigDecimal.valueOf(35.00),
                false, "http://example.com/hamburguer.jpg");
    }

    @Test
    @DisplayName("Deve delegar a execução para o AdicionarItemCardapioUseCase corretamente")
    void shouldDelegateExecutionToAdicionarItemCardapioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> adicionarItemCardapioInputPort.execute(restauranteId, itemInputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(adicionarItemCardapioUseCase, times(1)).execute(
                eq(restauranteId),
                any(ItemCardapioInputModel.class)
        );
    }
}