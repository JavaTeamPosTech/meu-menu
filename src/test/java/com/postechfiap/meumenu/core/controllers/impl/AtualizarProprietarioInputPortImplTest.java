package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.proprietario.impl.AtualizarProprietarioInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarProprietarioInputPortImplTest {

    @Mock
    private AtualizarProprietarioUseCase atualizarProprietarioUseCase;

    @InjectMocks
    private AtualizarProprietarioInputPortImpl atualizarProprietarioInputPort;

    private AtualizarProprietarioInputModel inputModel;
    private UUID proprietarioId;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        inputModel = new AtualizarProprietarioInputModel(
                proprietarioId, // ID do proprietário
                "Proprietario Atualizado",
                "atualizado.prop@email.com",
                "login.atualizado.prop",
                "99988877766", // CPF
                "11977776666", // Whatsapp
                StatusContaEnum.BLOQUEADO // Status Conta
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o AtualizarProprietarioUseCase corretamente")
    void shouldDelegateExecutionToAtualizarProprietarioUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> atualizarProprietarioInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(atualizarProprietarioUseCase, times(1)).execute(
                any(AtualizarProprietarioInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis no InputModel
        );
    }
}