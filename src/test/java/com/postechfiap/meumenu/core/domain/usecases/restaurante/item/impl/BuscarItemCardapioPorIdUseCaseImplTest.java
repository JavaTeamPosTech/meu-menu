package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.BuscarItemCardapioPorIdOutputPort;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarItemCardapioPorIdUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private BuscarItemCardapioPorIdOutputPort buscarItemCardapioPorIdOutputPort;

    @InjectMocks
    private BuscarItemCardapioPorIdUseCaseImpl buscarItemCardapioPorIdUseCase;

    private UUID restauranteId;
    private UUID itemId;
    private UUID proprietarioId;
    private RestauranteDomain restauranteDomainMock;
    private ProprietarioDomain proprietarioDomainMock;
    private ItemCardapioDomain itemEncontradoMock;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        proprietarioId = UUID.randomUUID();

        proprietarioDomainMock = mock(ProprietarioDomain.class);

        restauranteDomainMock = mock(RestauranteDomain.class);

        itemEncontradoMock = new ItemCardapioDomain(
                itemId, "Pizza M Teste", "Deliciosa pizza", BigDecimal.valueOf(50.00),
                false, "http://url.com/pizza.jpg", restauranteDomainMock
        );
}

    @Test
    @DisplayName("Deve retornar ItemCardapioDomain e notificar o Presenter se o restaurante e item forem encontrados")
    void shouldReturnItemCardapioDomainAndPresentSuccessWhenRestauranteAndItemFound() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomainMock));
        when(restauranteDomainMock.getItensCardapio()).thenReturn(new ArrayList<>(Collections.singletonList(itemEncontradoMock)));

        ItemCardapioDomain result = assertDoesNotThrow(() -> buscarItemCardapioPorIdUseCase.execute(restauranteId, itemId));

        assertNotNull(result);
        assertEquals(itemEncontradoMock, result); // O item retornado deve ser o mockado
        assertEquals(itemId, result.getId());

        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteDomainMock, times(1)).getItensCardapio();
        verify(buscarItemCardapioPorIdOutputPort, times(1)).presentSuccess(itemEncontradoMock); // Verifica notificação de sucesso
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buscarItemCardapioPorIdUseCase.execute(restauranteId, itemId));

        assertEquals("Restaurante com ID " + restauranteId + " não encontrado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteDomainMock, never()).getItensCardapio(); // Não deve tentar acessar itens
        verify(buscarItemCardapioPorIdOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class)); // Não deve notificar sucesso
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o item não for encontrado no cardápio do restaurante")
    void shouldThrowResourceNotFoundExceptionWhenItemNotFoundInMenu() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomainMock));

        UUID outroItemId = UUID.randomUUID();
        ItemCardapioDomain outroItem = new ItemCardapioDomain(outroItemId, "Outro Item", "Desc", BigDecimal.TEN, false, "url", restauranteDomainMock);
        when(restauranteDomainMock.getItensCardapio()).thenReturn(new ArrayList<>(Collections.singletonList(outroItem)));


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buscarItemCardapioPorIdUseCase.execute(restauranteId, itemId)); // itemId é diferente de outroItemId

        assertEquals("Item do cardápio com ID " + itemId + " não encontrado no restaurante ID " + restauranteId + ".", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteDomainMock, times(1)).getItensCardapio(); // Deve tentar acessar itens
        verify(buscarItemCardapioPorIdOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class)); // Não deve notificar sucesso
    }
}