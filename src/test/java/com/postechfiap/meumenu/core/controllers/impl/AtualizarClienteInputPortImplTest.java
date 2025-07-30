package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.cliente.AtualizarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarClienteInputPortImplTest {

    @Mock
    private AtualizarClienteUseCase atualizarClienteUseCase;

    @InjectMocks
    private AtualizarClienteInputPortImpl atualizarClienteInputPort;

    private AtualizarClienteInputModel inputModel;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        inputModel = new AtualizarClienteInputModel(
                clienteId,
                "Nome Atualizado",
                "email.atualizado@test.com",
                "login.atualizado",
                "98765432100", // CPF
                LocalDate.of(1995, 3, 25),
                GeneroEnum.FEMININO,
                "21998765432",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.VEGETARIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.DEBITO,
                false
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o AtualizarClienteUseCase corretamente")
    void shouldDelegateExecutionToAtualizarClienteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> atualizarClienteInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(atualizarClienteUseCase, times(1)).execute(
                any(AtualizarClienteInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis no InputModel
        );
    }
}