package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.presenters.DeletarClienteOutputPort;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarClienteUseCaseImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private DeletarClienteOutputPort deletarClienteOutputPort;

    @InjectMocks
    private DeletarClienteUseCaseImpl deletarClienteUseCase;

    private UUID clienteId;
    private ClienteDomain clienteDomain;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();

        clienteDomain = new ClienteDomain(
                clienteId, // ID
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
    }

    @Test
    @DisplayName("Deve deletar um cliente com sucesso se ele for encontrado")
    void shouldDeleteClientSuccessfullyWhenFound() {
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteDomain));
        doNothing().when(clienteGateway).deletarCliente(clienteId);

        assertDoesNotThrow(() -> deletarClienteUseCase.execute(clienteId));

        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(clienteGateway, times(1)).deletarCliente(clienteId);
        verify(deletarClienteOutputPort, times(1)).presentSuccess("Cliente com ID " + clienteId + " excluído com sucesso.");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o cliente não for encontrado para exclusão")
    void shouldThrowResourceNotFoundExceptionWhenClientIsNotFound() {
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deletarClienteUseCase.execute(clienteId));

        assertEquals("Cliente com ID " + clienteId + " não encontrado para exclusão.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(clienteGateway, never()).deletarCliente(any(UUID.class));
        verify(deletarClienteOutputPort, never()).presentSuccess(anyString());
    }

}