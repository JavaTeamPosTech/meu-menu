package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.DeletarRestauranteOutputPort;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarRestauranteUseCaseImplTest {

    @Mock private RestauranteGateway restauranteGateway;
    @Mock private DeletarRestauranteOutputPort deletarRestauranteOutputPort;

    @InjectMocks private DeletarRestauranteUseCaseImpl deletarRestauranteUseCase;

    private UUID restauranteId;
    private UUID proprietarioDonoId;
    private UUID outroProprietarioId;
    private RestauranteDomain restauranteDomain; // Declarado sem inicialização aqui
    private ProprietarioDomain proprietarioDono; // Declarado sem inicialização aqui

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        proprietarioDonoId = UUID.randomUUID();
        outroProprietarioId = UUID.randomUUID();

        proprietarioDono = mock(ProprietarioDomain.class);
        restauranteDomain = mock(RestauranteDomain.class);
    }

    @Test
    @DisplayName("Deve deletar um restaurante com sucesso se encontrado e proprietário é o dono")
    void shouldDeleteRestauranteSuccessfullyWhenFoundAndProprietarioIsOwner() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomain));
        when(restauranteDomain.getProprietario()).thenReturn(proprietarioDono);
        when(proprietarioDono.getId()).thenReturn(proprietarioDonoId);

        doNothing().when(restauranteGateway).deletarRestaurante(restauranteId);

        assertDoesNotThrow(() -> deletarRestauranteUseCase.execute(restauranteId, proprietarioDonoId));

        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, times(1)).deletarRestaurante(restauranteId);
        verify(deletarRestauranteOutputPort, times(1)).presentSuccess("Restaurante com ID " + restauranteId + " excluído com sucesso.");
        verify(restauranteDomain, times(1)).getProprietario();
        verify(proprietarioDono, times(1)).getId();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado para exclusão")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deletarRestauranteUseCase.execute(restauranteId, proprietarioDonoId));

        assertEquals("Restaurante com ID " + restauranteId + " não encontrado para exclusão.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, never()).deletarRestaurante(any(UUID.class));
        verify(deletarRestauranteOutputPort, never()).presentSuccess(anyString());
        verify(restauranteDomain, never()).getProprietario();
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o proprietário logado não for o dono do restaurante")
    void shouldThrowBusinessExceptionWhenProprietarioIsNotOwner() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomain));
        when(restauranteDomain.getProprietario()).thenReturn(proprietarioDono);
        when(proprietarioDono.getId()).thenReturn(proprietarioDonoId);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> deletarRestauranteUseCase.execute(restauranteId, outroProprietarioId));

        assertEquals("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, never()).deletarRestaurante(any(UUID.class));
        verify(deletarRestauranteOutputPort, never()).presentSuccess(anyString());
        verify(restauranteDomain, times(1)).getProprietario();
        verify(proprietarioDono, times(1)).getId();
    }
}