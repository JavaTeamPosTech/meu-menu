package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarClienteOutputPort;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarClientePorIdUseCaseImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private BuscarClienteOutputPort buscarClienteOutputPort;

    @InjectMocks
    private BuscarClientePorIdUseCaseImpl buscarClientePorIdUseCase;

    private UUID clienteId;
    private ClienteDomain clienteDomain;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        clienteDomain = new ClienteDomain(
                clienteId, // ID
                "12345678900", // CPF
                LocalDate.of(1990, 5, 15), // Data Nascimento
                GeneroEnum.FEMININO, // Gênero
                "11987654321", // Telefone
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)), // Preferencias
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.LACTOSE)), // Alergias
                MetodoPagamentoEnum.CREDITO, // Metodo Pagamento Preferido
                true, // Notificacoes Ativas
                false, // Cliente VIP
                0, // Saldo Pontos
                0, // Avaliacoes Feitas
                null, // Ultimo Pedido (LocalDateTime)
                "Maria Teste", // Nome
                "maria@teste.com", // Email
                "mariart", // Login
                "senha_hash", // Senha criptografada
                null, null, null // Datas de criação/atualização e Endereços serão setados pelo Use Case ou mapper
        );
    }

    @Test
    @DisplayName("Deve retornar Optional.of(ClienteDomain) e notificar o Presenter se o cliente for encontrado")
    void shouldReturnOptionalOfClienteDomainAndPresentSuccessWhenClientIsFound() {
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteDomain));

        Optional<ClienteDomain> result = assertDoesNotThrow(() -> buscarClientePorIdUseCase.execute(clienteId));

        assertTrue(result.isPresent());
        assertEquals(clienteDomain, result.get());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(buscarClienteOutputPort, times(1)).presentSuccess(clienteDomain);
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o cliente não for encontrado")
    void shouldThrowBusinessExceptionWhenClientIsNotFound() {
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> buscarClientePorIdUseCase.execute(clienteId));

        assertEquals("Cliente com ID " + clienteId + " não encontrado.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(buscarClienteOutputPort, never()).presentSuccess(any(ClienteDomain.class));
    }
}