package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarClienteOutputPort;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarClienteUseCaseImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private AtualizarClienteOutputPort atualizarClienteOutputPort;

    @InjectMocks
    private AtualizarClienteUseCaseImpl atualizarClienteUseCase;

    private UUID clienteId;
    private ClienteDomain clienteExistente;
    private AtualizarClienteInputModel inputModel;
    private ClienteDomain clienteAtualizadoRetornado;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();

        clienteExistente = new ClienteDomain(
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

        inputModel = new AtualizarClienteInputModel(
                clienteId, // ID do cliente a ser atualizado
                "Cliente Atualizado", // Nome
                "atualizado@email.com", // Email
                "loginatualizado", // Login
                "99988877766", // CPF
                LocalDate.of(1985, 10, 20), // Data Nascimento
                GeneroEnum.FEMININO, // Gênero
                "11977776666", // Telefone
                new HashSet<>(Collections.singletonList(TiposComidaEnum.JAPONESA)), // Preferencias
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.AMENDOIM)), // Alergias
                MetodoPagamentoEnum.CREDITO, // Metodo Pagamento Preferido
                false // Notificacoes Ativas
        );

        // ClienteDomain que seria retornado pelo gateway após a atualização
        clienteAtualizadoRetornado = new ClienteDomain(
                clienteId,
                inputModel.getCpf(),
                inputModel.getDataNascimento(),
                inputModel.getGenero(),
                inputModel.getTelefone(),
                inputModel.getPreferenciasAlimentares(),
                inputModel.getAlergias(),
                inputModel.getMetodoPagamentoPreferido(),
                inputModel.getNotificacoesAtivas(),
                false, // Cliente VIP
                0, // Saldo Pontos
                0, // Avaliacoes Feitas
                null, // Ultimo Pedido (LocalDateTime)
                inputModel.getNome(),
                inputModel.getEmail(),
                inputModel.getLogin(),
                "senha_hash_original", // Senha não muda
                clienteExistente.getDataCriacao(), // Data Criação não muda
                LocalDateTime.now(), // Data Atualização muda
                Collections.emptyList() // Endereços não mudam por aqui
        );
    }

    @Test
    @DisplayName("Deve atualizar um cliente com sucesso sem conflitos")
    void shouldUpdateClientSuccessfullyWithoutConflicts() {
        // Mocks de busca e validações
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false); // Novo email não existe
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(false); // Novo login não existe
        when(clienteGateway.existsByCpf(inputModel.getCpf())).thenReturn(false); // Novo CPF não existe

        // Mock para o gateway de atualização
        when(clienteGateway.atualizarCliente(any(ClienteDomain.class))).thenReturn(clienteAtualizadoRetornado);

        // Executa o Use Case
        ClienteDomain result = assertDoesNotThrow(() -> atualizarClienteUseCase.execute(inputModel));

        // Verifica o retorno
        assertNotNull(result);
        assertEquals(inputModel.getNome(), result.getNome());
        assertEquals(inputModel.getEmail(), result.getEmail());
        assertEquals(inputModel.getLogin(), result.getLogin());
        assertEquals(inputModel.getCpf(), result.getCpf());
        assertEquals(inputModel.getGenero(), result.getGenero());
        assertEquals(inputModel.getTelefone(), result.getTelefone());
        assertEquals(inputModel.getPreferenciasAlimentares(), result.getPreferenciasAlimentares());
        assertEquals(inputModel.getAlergias(), result.getAlergias());
        assertEquals(inputModel.getMetodoPagamentoPreferido(), result.getMetodoPagamentoPreferido());
        assertEquals(inputModel.getNotificacoesAtivas(), result.getNotificacoesAtivas());
        assertNotNull(result.getDataAtualizacao()); // Data de atualização deve ter mudado

        // Verifica as interações com mocks
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        // Verifica que existsByEmail/Login/Cpf foram chamados para o NOVO valor (diferente do original)
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(clienteGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(clienteGateway, times(1)).atualizarCliente(any(ClienteDomain.class)); // Verifica se o cliente foi passado para atualização
        verify(atualizarClienteOutputPort, times(1)).presentSuccess(any(ClienteDomain.class)); // Verifica notificação de sucesso
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso se email/login/CPF não foram alterados")
    void shouldUpdateClientSuccessfullyIfFieldsAreNotChanged() {
        // Cliente existente e input model têm os mesmos email, login, cpf
        inputModel = new AtualizarClienteInputModel(
                clienteId, clienteExistente.getNome(), clienteExistente.getEmail(), clienteExistente.getLogin(),
                clienteExistente.getCpf(), clienteExistente.getDataNascimento(), clienteExistente.getGenero(),
                clienteExistente.getTelefone(), clienteExistente.getPreferenciasAlimentares(),
                clienteExistente.getAlergias(), clienteExistente.getMetodoPagamentoPreferido(),
                clienteExistente.getNotificacoesAtivas()
        );

        // Mocks
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteExistente));
        // existsByEmail/Login/Cpf NÃO devem ser chamados se os valores não mudaram
        when(clienteGateway.atualizarCliente(any(ClienteDomain.class))).thenReturn(clienteAtualizadoRetornado);

        assertDoesNotThrow(() -> atualizarClienteUseCase.execute(inputModel));

        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(usuarioGateway, never()).existsByEmail(anyString()); // Não deve verificar email
        verify(usuarioGateway, never()).existsByLogin(anyString()); // Não deve verificar login
        verify(clienteGateway, never()).existsByCpf(anyString()); // Não deve verificar CPF
        verify(clienteGateway, times(1)).atualizarCliente(any(ClienteDomain.class));
        verify(atualizarClienteOutputPort, times(1)).presentSuccess(any(ClienteDomain.class));
    }


    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o cliente não for encontrado para atualização")
    void shouldThrowResourceNotFoundExceptionWhenClientNotFound() {
        // Mock: clienteGateway retorna Optional.empty
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.empty());

        // Executa o Use Case e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> atualizarClienteUseCase.execute(inputModel));

        // Verifica
        assertEquals("Cliente com ID " + clienteId + " não encontrado para atualização.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(usuarioGateway, never()).existsByEmail(anyString());
        verify(usuarioGateway, never()).existsByLogin(anyString());
        verify(clienteGateway, never()).existsByCpf(anyString());
        verify(clienteGateway, never()).atualizarCliente(any(ClienteDomain.class));
        verify(atualizarClienteOutputPort, never()).presentSuccess(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o email já existir para outro usuário")
    void shouldThrowBusinessExceptionWhenEmailAlreadyExists() {
        // Mock: cliente existente, mas email do input já existe para outro
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(true); // Email já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarClienteUseCase.execute(inputModel));

        assertEquals("Email '" + inputModel.getEmail() + "' já cadastrado por outro usuário.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, never()).existsByLogin(anyString());
        verify(clienteGateway, never()).existsByCpf(anyString());
        verify(clienteGateway, never()).atualizarCliente(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o login já existir para outro usuário")
    void shouldThrowBusinessExceptionWhenLoginAlreadyExists() {
        // Mock
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false); // Email não existe (passa)
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(true); // Login já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarClienteUseCase.execute(inputModel));

        assertEquals("Login '" + inputModel.getLogin() + "' já cadastrado por outro usuário.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(clienteGateway, never()).existsByCpf(anyString());
        verify(clienteGateway, never()).atualizarCliente(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o CPF já existir para outro cliente")
    void shouldThrowBusinessExceptionWhenCpfAlreadyExists() {
        // Mock
        when(clienteGateway.buscarClientePorId(clienteId)).thenReturn(Optional.of(clienteExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false);
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(false);
        when(clienteGateway.existsByCpf(inputModel.getCpf())).thenReturn(true); // CPF já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarClienteUseCase.execute(inputModel));

        assertEquals("CPF '" + inputModel.getCpf() + "' já cadastrado por outro cliente.", exception.getMessage());
        verify(clienteGateway, times(1)).buscarClientePorId(clienteId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(clienteGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(clienteGateway, never()).atualizarCliente(any(ClienteDomain.class));
    }
}