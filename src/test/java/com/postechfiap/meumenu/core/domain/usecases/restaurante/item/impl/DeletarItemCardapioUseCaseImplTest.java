package com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.DeletarItemCardapioOutputPort;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarItemCardapioUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private DeletarItemCardapioOutputPort deletarItemCardapioOutputPort;

    @InjectMocks
    private DeletarItemCardapioUseCaseImpl deletarItemCardapioUseCase;

    private UUID restauranteId;
    private UUID itemId;
    private UUID proprietarioDonoId;
    private UUID outroProprietarioId;
    private RestauranteDomain restauranteExistenteMock;
    private ProprietarioDomain proprietarioDonoMock;
    private ItemCardapioDomain itemParaRemoverMock; // Mock do item que esperamos remover

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        proprietarioDonoId = UUID.randomUUID();
        outroProprietarioId = UUID.randomUUID();

        // 1. Mocks de Domínio (Proprietario e Restaurante)
        proprietarioDonoMock = mock(ProprietarioDomain.class);
        restauranteExistenteMock = mock(RestauranteDomain.class);

        // 2. Mock do ItemCardapioDomain a ser removido (com o ID que será procurado)
        itemParaRemoverMock = mock(ItemCardapioDomain.class);
    }

    @Test
    @DisplayName("Deve deletar um item do cardápio com sucesso se o restaurante e item forem encontrados e o proprietário for o dono")
    void shouldDeleteItemCardapioSuccessfully() {
        // Cenário: Item é encontrado na lista e .remove() tem sucesso
        // Configura o mock do restaurante para retornar uma lista que contém o item esperado
        List<ItemCardapioDomain> listaDeItensDoRestaurante = new ArrayList<>();
        listaDeItensDoRestaurante.add(itemParaRemoverMock);
        when(restauranteExistenteMock.getItensCardapio()).thenReturn(listaDeItensDoRestaurante); // Lista REAL com o mock do item

        // Mocks para o comportamento do gateway
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteGateway.atualizarRestaurante(any(RestauranteDomain.class))).thenReturn(restauranteExistenteMock); // Retorna o mesmo mock após atualização
        // Configura o mock do proprietário para retornar o ID correto
        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);
        when(proprietarioDonoMock.getId()).thenReturn(proprietarioDonoId); // O ID do proprietário deve corresponder ao ID passado

        // Configura o mock do item para retornar o ID correto
        when(itemParaRemoverMock.getId()).thenReturn(itemId); // O ID do item deve corresponder ao ID passado

        // Executa o Use Case
        assertDoesNotThrow(() ->
                deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioDonoId));

        // Verifica: a lista de itens do restaurante deve estar vazia após a remoção (porque é uma ArrayList real)
        assertTrue(listaDeItensDoRestaurante.isEmpty());

        // Verifica as interações
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getProprietario();
        verify(proprietarioDonoMock, times(1)).getId();
        verify(restauranteExistenteMock, times(2)).getItensCardapio(); // A lista foi acessada
        verify(itemParaRemoverMock, times(1)).getId(); // O ID do item foi acessado para o filtro
        verify(restauranteGateway, times(1)).atualizarRestaurante(restauranteExistenteMock);
        verify(deletarItemCardapioOutputPort, times(1)).presentSuccess("Item do cardápio com ID " + itemId + " excluído com sucesso do restaurante ID " + restauranteId + ".");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado para deletar item")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        // Executa e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioDonoId));

        assertEquals("Restaurante com ID " + restauranteId + " não encontrado para deletar item.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, never()).getProprietario(); // Não deve chamar getters no mock se o restaurante não for encontrado
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(deletarItemCardapioOutputPort, never()).presentSuccess(anyString());
        verify(restauranteExistenteMock, never()).getItensCardapio(); // Não deve acessar a lista de itens
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o proprietário logado não for o dono do restaurante")
    void shouldThrowBusinessExceptionWhenProprietarioIsNotOwner() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        // Configura o mock do proprietário para retornar um ID diferente do proprietarioLogadoId
        when(proprietarioDonoMock.getId()).thenReturn(UUID.randomUUID()); // Simula ID diferente
        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);

        // Executa e verifica a exceção
        BusinessException exception = assertThrows(BusinessException.class,
                () -> deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioDonoId)); // Passa o ID proprietarioDonoId, que não corresponde ao mockado

        assertEquals("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getProprietario();
        verify(proprietarioDonoMock, times(1)).getId();
        verify(restauranteExistenteMock, never()).getItensCardapio(); // Não deve tentar acessar itens se falhar antes
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(deletarItemCardapioOutputPort, never()).presentSuccess(anyString());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o item não for encontrado no cardápio do restaurante")
    void shouldThrowResourceNotFoundExceptionWhenItemNotFoundInMenu() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));
        // Configura para que getItensCardapio() retorne uma lista que NÃO contém o itemId
        // Criamos um item diferente para garantir que não seja o 'itemParaRemoverMock'
        ItemCardapioDomain itemDiferente = mock(ItemCardapioDomain.class);
        when(itemDiferente.getId()).thenReturn(UUID.randomUUID()); // Garante que o ID é diferente do itemId esperado

        List<ItemCardapioDomain> listaComItemDiferente = new ArrayList<>();
        listaComItemDiferente.add(itemDiferente);
        when(restauranteExistenteMock.getItensCardapio()).thenReturn(listaComItemDiferente);

        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);
        when(proprietarioDonoMock.getId()).thenReturn(proprietarioDonoId);

        // Executa e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioDonoId));

        assertEquals("Item do cardápio com ID " + itemId + " não encontrado no restaurante ID " + restauranteId + ".", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(1)).getItensCardapio();
        verify(itemParaRemoverMock, never()).getId(); // O ID do item que esperamos remover não deve ser acessado
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(deletarItemCardapioOutputPort, never()).presentSuccess(anyString());
    }

    @Test
    @DisplayName("Deve lançar BusinessException se a remoção interna do item da coleção falhar (cenário inesperado)")
    void shouldThrowBusinessExceptionIfInternalItemRemovalFails() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));

        // Configura o mock da lista para que .remove() retorne false para o item esperado
        List<ItemCardapioDomain> mockList = mock(List.class); // Cria um mock de List
        when(restauranteExistenteMock.getItensCardapio()).thenReturn(mockList); // Retorna o mock da lista
        // Para que o filter encontre o item, mas o remove falhe:
        when(mockList.stream()).thenReturn(Stream.of(itemParaRemoverMock)); // Para que o stream retorne o item

        // Simula que o .remove() falhou neste mockList (retorna false)
        when(mockList.remove(itemParaRemoverMock)).thenReturn(false);

        // Mocks para o comportamento do gateway
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistenteMock));

        when(restauranteExistenteMock.getProprietario()).thenReturn(proprietarioDonoMock);
        when(proprietarioDonoMock.getId()).thenReturn(proprietarioDonoId);

        when(restauranteExistenteMock.getItensCardapio()).thenReturn(mockList); // Retorna o mock da lista
        when(itemParaRemoverMock.getId()).thenReturn(itemId); // O ID do item deve corresponder ao ID passado

        BusinessException exception = assertThrows(BusinessException.class,
                () -> deletarItemCardapioUseCase.execute(restauranteId, itemId, proprietarioDonoId));

        assertEquals("Falha interna ao remover item da coleção do restaurante.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteExistenteMock, times(2)).getItensCardapio();
        verify(mockList, times(1)).remove(itemParaRemoverMock); // Verifica que remove foi chamado na lista mockada
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(deletarItemCardapioOutputPort, never()).presentSuccess(anyString());
    }
}