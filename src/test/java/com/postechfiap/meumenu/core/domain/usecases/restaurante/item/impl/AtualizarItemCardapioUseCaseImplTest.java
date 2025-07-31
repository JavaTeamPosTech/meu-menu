package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.AtualizarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.dtos.restaurante.item.ItemCardapioInputModel;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarItemCardapioUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private AtualizarItemCardapioOutputPort atualizarItemCardapioOutputPort;

    @InjectMocks
    private AtualizarItemCardapioUseCaseImpl atualizarItemCardapioUseCase;

    private UUID restauranteId;
    private UUID itemId;
    private UUID proprietarioDonoId;
    private UUID outroProprietarioId;
    private RestauranteDomain restauranteExistenteMock;
    private ProprietarioDomain proprietarioDonoMock;
    private ItemCardapioDomain itemExistenteMock;
    private ItemCardapioInputModel inputModelAtualizacao;
    private ItemCardapioDomain itemAtualizadoRetornado;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        proprietarioDonoId = UUID.randomUUID();
        outroProprietarioId = UUID.randomUUID();

        // Mocks de ProprietarioDomain e RestauranteDomain
        proprietarioDonoMock = mock(ProprietarioDomain.class);

        restauranteExistenteMock = mock(RestauranteDomain.class);

        // Item existente no cardápio do restaurante
        itemExistenteMock = new ItemCardapioDomain(
                itemId, "Pizza Antiga", "Desc Antiga", BigDecimal.valueOf(40.00), false, "http://old.jpg", restauranteExistenteMock
        );

        // InputModel com os novos dados para atualização
        inputModelAtualizacao = new ItemCardapioInputModel(
                "Pizza Nova", "Desc Nova", BigDecimal.valueOf(55.00), true, "http://new.jpg");

        // ItemDomain que seria retornado pelo gateway após a atualização (simulando o itemExistente com os dados do inputModel)
        itemAtualizadoRetornado = new ItemCardapioDomain(
                itemId, // O mesmo ID, pois é uma atualização
                inputModelAtualizacao.getNome(),
                inputModelAtualizacao.getDescricao(),
                inputModelAtualizacao.getPreco(),
                inputModelAtualizacao.getDisponivelApenasNoRestaurante(),
                inputModelAtualizacao.getUrlFoto(),
                restauranteExistenteMock
        );
    }

    @Test
    @DisplayName("Deve atualizar um item do cardápio com sucesso se o restaurante e item forem encontrados e o proprietário for o dono")
    void shouldUpdateItemCardapioSuccessfully() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);
        when(proprietarioDonoMock.getId()).thenReturn(proprietarioDonoId);
        when(restauranteGateway.atualizarItemCardapio(any(ItemCardapioDomain.class))).thenReturn(itemAtualizadoRetornado);
        when(restauranteExistenteMock.getItensCardapio()).thenReturn(Arrays.asList(itemExistenteMock));

        // Executa o Use Case
        ItemCardapioDomain result = assertDoesNotThrow(() ->
                atualizarItemCardapioUseCase.execute(restauranteId, itemId, inputModelAtualizacao, proprietarioDonoId));

        // Verifica o retorno
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(inputModelAtualizacao.getNome(), result.getNome());
        assertEquals(inputModelAtualizacao.getDescricao(), result.getDescricao());
        assertEquals(inputModelAtualizacao.getPreco(), result.getPreco());

        // Verifica as interações
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getProprietario();
        verify(proprietarioDonoMock, times(1)).getId();
        verify(restauranteExistenteMock, times(1)).getItensCardapio();
        verify(restauranteGateway, times(1)).atualizarItemCardapio(itemExistenteMock); // Verifica se o item EXISTENTE foi passado
        verify(atualizarItemCardapioOutputPort, times(1)).presentSuccess(itemAtualizadoRetornado);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado para atualizar item")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        // Executa e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> atualizarItemCardapioUseCase.execute(restauranteId, itemId, inputModelAtualizacao, proprietarioDonoId));

        assertEquals("Restaurante com ID " + restauranteId + " não encontrado para atualizar item.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, never()).getProprietario(); // Não deve chamar getters no mock se o restaurante não for encontrado
        verify(restauranteGateway, never()).atualizarItemCardapio(any(ItemCardapioDomain.class));
        verify(atualizarItemCardapioOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o proprietário logado não for o dono do restaurante")
    void shouldThrowBusinessExceptionWhenProprietarioIsNotOwner() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        // Proprietario mock retorna um ID diferente do proprietarioLogadoId
        when(proprietarioDonoMock.getId()).thenReturn(UUID.randomUUID()); // Simula ID diferente
        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);

        // Executa e verifica a exceção
        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarItemCardapioUseCase.execute(restauranteId, itemId, inputModelAtualizacao, proprietarioDonoId));

        assertEquals("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getProprietario();
        verify(proprietarioDonoMock, times(1)).getId();
        verify(restauranteGateway, never()).atualizarItemCardapio(any(ItemCardapioDomain.class));
        verify(atualizarItemCardapioOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o item não for encontrado no cardápio do restaurante")
    void shouldThrowResourceNotFoundExceptionWhenItemNotFoundInMenu() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        // restauranteExistenteMock.getItensCardapio() já está mockado para retornar lista vazia
        // Ou, para ser mais explícito neste teste:
        when(restauranteExistenteMock.getItensCardapio()).thenReturn(Collections.emptyList());
        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);
        when(proprietarioDonoMock.getId()).thenReturn(proprietarioDonoId);

        // Executa e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> atualizarItemCardapioUseCase.execute(restauranteId, itemId, inputModelAtualizacao, proprietarioDonoId));

        assertEquals("Item do cardápio com ID " + itemId + " não encontrado no restaurante ID " + restauranteId + ".", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getItensCardapio();
        verify(restauranteGateway, never()).atualizarItemCardapio(any(ItemCardapioDomain.class));
        verify(atualizarItemCardapioOutputPort, never()).presentSuccess(any(ItemCardapioDomain.class));
    }
}