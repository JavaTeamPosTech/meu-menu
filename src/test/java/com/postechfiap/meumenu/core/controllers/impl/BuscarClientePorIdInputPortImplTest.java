package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.cliente.impl.BuscarClientePorIdInputPortImpl;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.usecases.cliente.BuscarClientePorIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarClientePorIdInputPortImplTest {

    @Mock
    private BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    @InjectMocks
    private BuscarClientePorIdInputPortImpl buscarClientePorIdInputPort;

    private UUID clienteId;
    private ClienteDomain clienteDomain;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        // Criar uma instância de ClienteDomain para simular o retorno do UseCase
        clienteDomain = new ClienteDomain(
                clienteId, "12345678900", LocalDate.of(1990, 1, 1), GeneroEnum.MASCULINO,
                "11987654321", Collections.emptySet(), Collections.emptySet(), null, false, false, 0, 0, null,
                "Cliente Teste", "teste@email.com", "login", "senha_hash", null, null, Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve delegar a busca e retornar Optional.of(ClienteDomain) se encontrado")
    void shouldDelegateAndReturnOptionalOfClienteDomainWhenFound() {
        // Mock: UseCase retorna Optional.of com o cliente
        when(buscarClientePorIdUseCase.execute(eq(clienteId))).thenReturn(Optional.of(clienteDomain));

        // Executa o método do InputPortImpl
        Optional<ClienteDomain> result = assertDoesNotThrow(() -> buscarClientePorIdInputPort.execute(clienteId));

        // Verifica
        assertTrue(result.isPresent());
        assertEquals(clienteDomain, result.get());
        verify(buscarClientePorIdUseCase, times(1)).execute(eq(clienteId)); // Verifica a delegação
    }

    @Test
    @DisplayName("Deve delegar a busca e retornar Optional.empty() se não encontrado")
    void shouldDelegateAndReturnOptionalEmptyWhenNotFound() {
        // Mock: UseCase retorna Optional.empty
        when(buscarClientePorIdUseCase.execute(eq(clienteId))).thenReturn(Optional.empty());

        // Executa o método do InputPortImpl
        Optional<ClienteDomain> result = assertDoesNotThrow(() -> buscarClientePorIdInputPort.execute(clienteId));

        // Verifica
        assertTrue(result.isEmpty());
        verify(buscarClientePorIdUseCase, times(1)).execute(eq(clienteId)); // Verifica a delegação
    }
}