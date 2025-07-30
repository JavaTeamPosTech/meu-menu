package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.AdicionarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdicionarItemCardapioUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private AdicionarItemCardapioOutputPort adicionarItemCardapioOutputPort;

    @InjectMocks
    private AdicionarItemCardapioUseCaseImpl adicionarItemCardapioUseCase;

    private UUID restauranteId;
    private UUID proprietarioId;
    private RestauranteDomain restauranteDomain;
    private ProprietarioDomain proprietarioDomainMock;
    private ItemCardapioInputModel itemInputModel;
    private ItemCardapioDomain novoItemDomain;
    private ItemCardapioDomain itemSalvoDomain;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        proprietarioId = UUID.randomUUID();

        // Mock de ProprietarioDomain
        proprietarioDomainMock = mock(ProprietarioDomain.class);

        // Mock de RestauranteDomain
        restauranteDomain = mock(RestauranteDomain.class);

        // Garante que a lista de itens não é nula quando adicionamos (mesmo que vazia no mock)

        // ItemCardapioInputModel
        itemInputModel = new ItemCardapioInputModel(
                "Pizza Teste", "Deliciosa pizza de teste", BigDecimal.valueOf(50.00),
                false, "http://url.com/pizza.jpg");

        // ItemCardapioDomain que seria criado no UseCase
        novoItemDomain = new ItemCardapioDomain(
                itemInputModel.getNome(), itemInputModel.getDescricao(), itemInputModel.getPreco(),
                itemInputModel.getDisponivelApenasNoRestaurante(), itemInputModel.getUrlFoto());
        novoItemDomain.setId(UUID.randomUUID()); // Simula o ID gerado temporariamente no domínio
        novoItemDomain.setRestaurante(restauranteDomain); // Simula associação

        // ItemCardapioDomain que seria retornado pelo Gateway após o save (com ID do banco)
        itemSalvoDomain = new ItemCardapioDomain(
                UUID.randomUUID(), // Simula o ID REAL gerado pelo banco
                novoItemDomain.getNome(), novoItemDomain.getDescricao(), novoItemDomain.getPreco(),
                novoItemDomain.getDisponivelApenasNoRestaurante(), novoItemDomain.getUrlFoto(),
                restauranteDomain
        );
    }

    @Test
    @DisplayName("Deve adicionar um item ao cardápio com sucesso se o restaurante for encontrado")
    void shouldAddItemCardapioSuccessfullyWhenRestauranteFound() {
        // Mock: restauranteGateway.buscarRestaurantePorId retorna Optional.of com o restaurante
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteDomain));
        // Mock: restauranteGateway.adicionarItemCardapio retorna o item salvo com ID do banco
        when(restauranteGateway.adicionarItemCardapio(any(UUID.class), any(ItemCardapioDomain.class)))
                .thenReturn(itemSalvoDomain);

        // Executa o Use Case
        ItemCardapioDomain result = assertDoesNotThrow(() -> adicionarItemCardapioUseCase.execute(restauranteId, itemInputModel));

        // Verifica o retorno
        assertNotNull(result);
        assertEquals(itemSalvoDomain, result); // O item retornado deve ser o que simulamos como salvo
        assertNotNull(result.getId()); // Deve ter um ID (do banco)

        // Verifica as interações
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        // Verifica a chamada ao gateway para adicionar item
        verify(restauranteGateway, times(1)).adicionarItemCardapio(eq(restauranteId), any(ItemCardapioDomain.class));
        // Verifica que o Presenter foi notificado com o item salvo
        verify(adicionarItemCardapioOutputPort, times(1)).presentSuccess(itemSalvoDomain);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado para adicionar item")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        // Mock: restauranteGateway.buscarRestaurantePorId retorna Optional.empty
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        // Executa o Use Case e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adicionarItemCardapioUseCase.execute(restauranteId, itemInputModel));

        // Verifica
        assertEquals("Restaurante com ID " + restauranteId + " não encontrado para adicionar item.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, never()).adicionarItemCardapio(any(UUID.class), any(ItemCardapioDomain.class)); // Não deve tentar adicionar
        verify(adicionarItemCardapioOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class)); // Não deve notificar sucesso
    }

    // Você pode adicionar mais testes aqui para regras de negócio específicas se houver,
    // como por exemplo, itens com o mesmo nome no cardápio do mesmo restaurante.
}