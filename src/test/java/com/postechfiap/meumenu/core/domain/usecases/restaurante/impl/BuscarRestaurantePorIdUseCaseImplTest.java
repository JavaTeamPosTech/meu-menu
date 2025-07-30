package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarRestaurantePorIdOutputPort;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
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
class BuscarRestaurantePorIdUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private BuscarRestaurantePorIdOutputPort buscarRestaurantePorIdOutputPort;

    @InjectMocks
    private BuscarRestaurantePorIdUseCaseImpl buscarRestaurantePorIdUseCase;

    private UUID restauranteId;
    private RestauranteDomain restauranteDomain;
    private ProprietarioDomain proprietarioMock;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        // Criar um ProprietarioDomain simples para o RestauranteDomain
        proprietarioMock = new ProprietarioDomain(
                UUID.randomUUID(), "11122233344", "11988887777", null,
                "Dono Teste", "dono@teste.com", "dono.login", "hash_senha",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );

        // Criar uma instância de RestauranteDomain para simular um retorno de sucesso
        restauranteDomain = new RestauranteDomain(
                restauranteId, "00000000000100", "Razao Social Ltda", "Restaurante Teste",
                "ISENTO", "1122334455", proprietarioMock, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve retornar RestauranteDomain e notificar o Presenter se o restaurante for encontrado")
    void shouldReturnRestauranteDomainAndPresentSuccessWhenRestauranteIsFound() {
        // Mock: restauranteGateway.buscarRestaurantePorId retorna Optional.of com o restaurante
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomain));

        // Executa o Use Case
        RestauranteDomain result = assertDoesNotThrow(() -> buscarRestaurantePorIdUseCase.execute(restauranteId));

        // Verifica
        assertNotNull(result);
        assertEquals(restauranteDomain, result); // Verifica se o objeto retornado é o mesmo
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(buscarRestaurantePorIdOutputPort, times(1)).presentSuccess(restauranteDomain); // Verifica notificação de sucesso
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteIsNotFound() {
        // Mock: restauranteGateway.buscarRestaurantePorId retorna Optional.empty
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        // Executa o Use Case e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buscarRestaurantePorIdUseCase.execute(restauranteId));

        // Verifica
        assertEquals("Restaurante com ID " + restauranteId + " não encontrado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(buscarRestaurantePorIdOutputPort, never()).presentSuccess(any(RestauranteDomain.class)); // Verifica que presentSuccess NÃO foi chamado
    }
}