package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.BuscarTodosRestaurantesOutputPort;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarTodosRestaurantesUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private BuscarTodosRestaurantesOutputPort buscarTodosRestaurantesOutputPort;

    @InjectMocks
    private BuscarTodosRestaurantesUseCaseImpl buscarTodosRestaurantesUseCase;

    private RestauranteDomain restaurante1;
    private RestauranteDomain restaurante2;
    private ProprietarioDomain proprietarioMock;

    @BeforeEach
    void setUp() {
        proprietarioMock = new ProprietarioDomain(
                UUID.randomUUID(), "11122233344", "11988887777", null, // StatusContaEnum.ATIVO
                "Proprietario Teste", "prop@test.com", "proplogin", "hash",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList(), Collections.emptyList()
        );

        restaurante1 = new RestauranteDomain(
                UUID.randomUUID(), "00000000000100", "RS1", "NomeF1", "IE1", "Tel1",
                proprietarioMock, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        restaurante2 = new RestauranteDomain(
                UUID.randomUUID(), "00000000000200", "RS2", "NomeF2", "IE2", "Tel2",
                proprietarioMock, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve retornar uma lista de restaurantes e notificar o Presenter em caso de sucesso")
    void shouldReturnListOfRestaurantsAndPresentSuccess() {
        List<RestauranteDomain> restaurantesEncontrados = List.of(restaurante1, restaurante2);

        when(restauranteGateway.buscarTodosRestaurantes()).thenReturn(restaurantesEncontrados);

        List<RestauranteDomain> result = buscarTodosRestaurantesUseCase.execute();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(restaurantesEncontrados, result); // Verifica se a lista retornada é a mesma
        verify(restauranteGateway, times(1)).buscarTodosRestaurantes(); // Verifica a interação com o gateway
        verify(buscarTodosRestaurantesOutputPort, times(1)).presentSuccess(restaurantesEncontrados); // Verifica notificação de sucesso
        verify(buscarTodosRestaurantesOutputPort, never()).presentNoContent(anyString()); // Verifica que presentNoContent NÃO foi chamado
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia e notificar o Presenter em caso de nenhum restaurante encontrado")
    void shouldReturnEmptyListAndPresentNoContent() {
        when(restauranteGateway.buscarTodosRestaurantes()).thenReturn(Collections.emptyList());

        List<RestauranteDomain> result = buscarTodosRestaurantesUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restauranteGateway, times(1)).buscarTodosRestaurantes(); // Verifica a interação com o gateway
        verify(buscarTodosRestaurantesOutputPort, times(1)).presentNoContent("Nenhum restaurante encontrado."); // Verifica notificação de no content
        verify(buscarTodosRestaurantesOutputPort, never()).presentSuccess(anyList()); // Verifica que presentSuccess NÃO foi chamado
    }
}