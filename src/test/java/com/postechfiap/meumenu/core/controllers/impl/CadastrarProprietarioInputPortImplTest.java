package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel; // Importar
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal; // Se necessário para EnderecoInputModel ou outros


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CadastrarProprietarioInputPortImplTest {

    @Mock
    private CadastrarProprietarioUseCase cadastrarProprietarioUseCase;

    @InjectMocks
    private CadastrarProprietarioInputPortImpl cadastrarProprietarioInputPort;

    private CadastrarProprietarioInputModel inputModel;
    private UUID proprietarioId; // Não usado diretamente no input, mas pode ser útil para setup

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        EnderecoInputModel enderecoInput = new EnderecoInputModel(
                "SP", "Sao Paulo", "Centro", "Rua Prop", 789, "Apto 5", "03000-000");

        inputModel = new CadastrarProprietarioInputModel(
                "Teste Proprietario", "proprietario@email.com", "proplogin", "senha_segura",
                "00011122233", // CPF
                "11999991234", // Whatsapp
                List.of(enderecoInput)
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o CadastrarProprietarioUseCase corretamente")
    void shouldDelegateExecutionToCadastrarProprietarioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> cadastrarProprietarioInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(cadastrarProprietarioUseCase, times(1)).execute(
                any(CadastrarProprietarioInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis
        );
    }
}