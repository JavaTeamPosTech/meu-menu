package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.AtualizarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
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
class AtualizarItemCardapioInputPortImplTest {

    @Mock
    private AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;

    @InjectMocks
    private AtualizarItemCardapioInputPortImpl atualizarItemCardapioInputPort;

    private UUID restauranteId;
    private UUID itemId;
    private UUID proprietarioLogadoId;
    private ItemCardapioInputModel itemInputModel;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        proprietarioLogadoId = UUID.randomUUID();
        itemInputModel = new ItemCardapioInputModel(
                "Hamburguer Atualizado", "Delicioso hamburguer com novas batatas", BigDecimal.valueOf(38.50),
                true, "http://example.com/hamburguer_novo.jpg");
    }

    @Test
    @DisplayName("Deve delegar a execução para o AtualizarItemCardapioUseCase corretamente")
    void shouldDelegateExecutionToAtualizarItemCardapioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> atualizarItemCardapioInputPort.execute(restauranteId, itemId, itemInputModel, proprietarioLogadoId));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(atualizarItemCardapioUseCase, times(1)).execute(
                eq(restauranteId),
                eq(itemId),
                any(ItemCardapioInputModel.class), // Pode usar eq(itemInputModel) se equals/hashCode forem confiáveis
                eq(proprietarioLogadoId)
        );
    }
}