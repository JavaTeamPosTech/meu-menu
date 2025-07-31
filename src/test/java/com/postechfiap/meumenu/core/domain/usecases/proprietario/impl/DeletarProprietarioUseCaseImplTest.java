package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.DeletarProprietarioOutputPort;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarProprietarioUseCaseImplTest {

    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private DeletarProprietarioOutputPort deletarProprietarioOutputPort;

    @InjectMocks
    private DeletarProprietarioUseCaseImpl deletarProprietarioUseCase;

    private UUID proprietarioId;
    private ProprietarioDomain proprietarioDomain;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        proprietarioDomain = mock(ProprietarioDomain.class);
    }

    @Test
    @DisplayName("Deve deletar um proprietário com sucesso se ele for encontrado")
    void shouldDeleteProprietarioSuccessfullyWhenFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));
        doNothing().when(proprietarioGateway).deletarProprietario(proprietarioId);

        assertDoesNotThrow(() -> deletarProprietarioUseCase.execute(proprietarioId));

        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(proprietarioGateway, times(1)).deletarProprietario(proprietarioId);
        verify(deletarProprietarioOutputPort, times(1)).presentSuccess("Proprietário com ID " + proprietarioId + " excluído com sucesso.");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o proprietário não for encontrado para exclusão")
    void shouldThrowResourceNotFoundExceptionWhenProprietarioIsNotFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deletarProprietarioUseCase.execute(proprietarioId));

        assertEquals("Proprietário com ID " + proprietarioId + " não encontrado para exclusão.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(proprietarioGateway, never()).deletarProprietario(any(UUID.class)); // Verifica que deletarProprietario NÃO foi chamado
        verify(deletarProprietarioOutputPort, never()).presentSuccess(anyString()); // Verifica que presentSuccess NÃO foi chamado
    }
}