package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.BuscarProprietarioOutputPort;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarProprietarioPorIdUseCaseImplTest {

    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private BuscarProprietarioOutputPort buscarProprietarioOutputPort;

    @InjectMocks
    private BuscarProprietarioPorIdUseCaseImpl buscarProprietarioPorIdUseCase;

    private UUID proprietarioId;
    private ProprietarioDomain proprietarioDomain;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        proprietarioDomain = new ProprietarioDomain(
                proprietarioId,
                "12345678900", // CPF
                "11987654321", // Whatsapp
                StatusContaEnum.ATIVO,
                "Nome Proprietario",
                "email@prop.com",
                "loginprop",
                "senha_hash",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve retornar ProprietarioDomain e notificar o Presenter se o proprietário for encontrado")
    void shouldReturnProprietarioDomainAndPresentSuccessWhenProprietarioIsFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));

        assertDoesNotThrow(() -> buscarProprietarioPorIdUseCase.execute(proprietarioId));


        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(buscarProprietarioOutputPort, times(1)).presentSuccess(proprietarioDomain);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o proprietário não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenProprietarioIsNotFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buscarProprietarioPorIdUseCase.execute(proprietarioId));

        assertEquals("Proprietário com ID " + proprietarioId + " não encontrado.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(buscarProprietarioOutputPort, never()).presentSuccess(any(ProprietarioDomain.class));
    }
}