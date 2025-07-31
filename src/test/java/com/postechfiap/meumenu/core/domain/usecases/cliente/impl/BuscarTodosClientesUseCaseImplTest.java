package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarTodosClientesUseCaseImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private BuscarTodosClientesOutputPort buscarTodosClientesOutputPort;

    @InjectMocks
    private BuscarTodosClientesUseCaseImpl buscarTodosClientesUseCase;

    private ClienteDomain cliente1;
    private ClienteDomain cliente2;

    @BeforeEach
    void setUp() {
        cliente1 = new ClienteDomain(
                UUID.randomUUID(), // ID
                "11122233344", // CPF original
                LocalDate.of(1985, 10, 20),
                GeneroEnum.MASCULINO,
                "11999998888",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.BRASILEIRA)),
                new HashSet<>(),
                MetodoPagamentoEnum.PIX,
                true, false, 0, 0, null,
                "Cliente Original", "original@email.com", "loginoriginal", "senha_hash_original",
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10), Collections.emptyList()
        );
        cliente2 = new ClienteDomain(
                UUID.randomUUID(), // ID
                "11122233322", // CPF original
                LocalDate.of(1984, 10, 22),
                GeneroEnum.MASCULINO,
                "11999998822",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.JAPONESA)),
                new HashSet<>(),
                MetodoPagamentoEnum.CREDITO,
                true, false, 0, 0, null,
                "Cliente 2", "cliente2@email.com", "cliente2", "senha_hash_original",
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10), Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes e notificar o Presenter em caso de sucesso")
    void shouldReturnListOfClientsAndPresentSuccess() {
        List<ClienteDomain> clientesEncontrados = List.of(cliente1, cliente2);

        when(clienteGateway.buscarTodosClientes()).thenReturn(clientesEncontrados);

        List<ClienteDomain> result = buscarTodosClientesUseCase.execute();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(clientesEncontrados, result);
        verify(clienteGateway, times(1)).buscarTodosClientes();
        verify(buscarTodosClientesOutputPort, times(1)).presentSuccess(clientesEncontrados);
        verify(buscarTodosClientesOutputPort, never()).presentNoContent(anyString());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia e notificar o Presenter em caso de nenhum cliente encontrado")
    void shouldReturnEmptyListAndPresentNoContent() {
        when(clienteGateway.buscarTodosClientes()).thenReturn(Collections.emptyList());

        List<ClienteDomain> result = buscarTodosClientesUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clienteGateway, times(1)).buscarTodosClientes();
        verify(buscarTodosClientesOutputPort, times(1)).presentNoContent("Nenhum cliente encontrado.");
        verify(buscarTodosClientesOutputPort, never()).presentSuccess(anyList());
    }
}