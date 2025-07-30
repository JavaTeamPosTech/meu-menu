package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.cliente.CadastrarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteInputPortImplTest {

    @Mock
    private CadastrarClienteUseCase cadastrarClienteUseCase;

    @InjectMocks
    private CadastrarClienteInputPortImpl cadastrarClienteInputPort;

    private CadastrarClienteInputModel inputModel;
    private UUID clienteId; // Não usado diretamente no input, mas pode ser útil para setup

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        EnderecoInputModel enderecoInput = new EnderecoInputModel(
                "SP", "Sao Paulo", "Centro", "Rua A", 123, "Apto 1", "01000-000");

        inputModel = new CadastrarClienteInputModel(
                "Teste Cliente", "cliente@email.com", "testelogin", "senha123", "12345678900",
                LocalDate.of(1990, 1, 1), GeneroEnum.MASCULINO, "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX, true, List.of(enderecoInput)
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o CadastrarClienteUseCase corretamente")
    void shouldDelegateExecutionToCadastrarClienteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> cadastrarClienteInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(cadastrarClienteUseCase, times(1)).execute(
                any(CadastrarClienteInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis
        );
    }
}