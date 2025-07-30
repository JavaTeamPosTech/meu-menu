package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.domain.presenters.CadastrarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteUseCaseImplTest {

    @Mock
    private ClienteGateway clienteGateway;
    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private PasswordService passwordService;
    @Mock
    private CadastrarClienteOutputPort clienteOutputPort;

    @InjectMocks
    private CadastrarClienteUseCaseImpl cadastrarClienteUseCase;

    private CadastrarClienteInputModel inputModel;
    private ClienteDomain novoClienteMock;
    private EnderecoInputModel enderecoInputModel;
    private EnderecoDomain enderecoDomainExpected;

    @BeforeEach
    void setUp() {
        enderecoInputModel = new EnderecoInputModel(
                "SP", "Sao Paulo", "Centro", "Rua A", 123, "Apto 1", "01000-000");

        inputModel = new CadastrarClienteInputModel(
                "Teste Nome", "teste@email.com", "testelogin", "senha123", "12345678900",
                LocalDate.of(1990, 1, 1), GeneroEnum.MASCULINO, "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX, true, List.of(enderecoInputModel)
        );

        novoClienteMock = new ClienteDomain(
                inputModel.getNome(),
                inputModel.getEmail(),
                inputModel.getLogin(),
                "senha_hash",
                inputModel.getCpf(),
                inputModel.getDataNascimento(),
                inputModel.getGenero(),
                inputModel.getTelefone(),
                inputModel.getPreferenciasAlimentares(),
                inputModel.getAlergias(),
                inputModel.getMetodoPagamentoPreferido(),
                inputModel.getNotificacoesAtivas(),
                false
        );

        novoClienteMock.setId(UUID.randomUUID());
        novoClienteMock.setDataCriacao(LocalDateTime.now());
        novoClienteMock.setDataAtualizacao(LocalDateTime.now());

        enderecoDomainExpected = new EnderecoDomain(
                enderecoInputModel.getEstado(), enderecoInputModel.getCidade(), enderecoInputModel.getBairro(),
                enderecoInputModel.getRua(), enderecoInputModel.getNumero(), enderecoInputModel.getComplemento(),
                enderecoInputModel.getCep()
        );
        enderecoDomainExpected.setId(UUID.randomUUID());
        enderecoDomainExpected.setUsuario(novoClienteMock);
    }

    @Test
    @DisplayName("Deve cadastrar um novo cliente com sucesso")
    void shouldCadastrarClienteSuccessfully() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(clienteGateway.existsByCpf(anyString())).thenReturn(false);
        when(passwordService.encryptPassword(anyString())).thenReturn("senha_hash");

        when(clienteGateway.cadastrarCliente(any(ClienteDomain.class)))
                .thenAnswer(invocation -> {
                    ClienteDomain clientePassed = invocation.getArgument(0);
                    clientePassed.setId(UUID.randomUUID());
                    if (clientePassed.getEnderecos() != null) {
                        clientePassed.getEnderecos().forEach(e -> e.setId(UUID.randomUUID()));
                    }
                    return clientePassed;
                });

        assertDoesNotThrow(() -> cadastrarClienteUseCase.execute(inputModel));

        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(clienteGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(passwordService, times(1)).encryptPassword(inputModel.getSenha());
        verify(clienteGateway, times(1)).cadastrarCliente(any(ClienteDomain.class));
        verify(clienteOutputPort, times(1)).presentSuccess(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se login já existir")
    void shouldThrowBusinessExceptionWhenLoginExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarClienteUseCase.execute(inputModel));

        assertEquals("Login já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, never()).existsByEmail(anyString());
        verify(clienteOutputPort, times(1)).presentError("Login já cadastrado.");
    }

    @Test
    @DisplayName("Deve lançar BusinessException se email já existir")
    void shouldThrowBusinessExceptionWhenEmailExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarClienteUseCase.execute(inputModel));

        assertEquals("Email já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(clienteOutputPort, times(1)).presentError("Email já cadastrado.");
    }

    @Test
    @DisplayName("Deve lançar BusinessException se CPF já existir")
    void shouldThrowBusinessExceptionWhenCpfExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(clienteGateway.existsByCpf(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarClienteUseCase.execute(inputModel));

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(clienteGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(clienteOutputPort, times(1)).presentError("CPF já cadastrado.");
    }

    @Test
    @DisplayName("Deve associar EnderecoDomain ao ClienteDomain e ao UsuarioDomain pai")
    void shouldAssociateEnderecoDomainCorrectly() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(clienteGateway.existsByCpf(anyString())).thenReturn(false);
        when(passwordService.encryptPassword(anyString())).thenReturn("senha_hash");

        doAnswer(invocation -> {
            ClienteDomain clientePassedToGateway = invocation.getArgument(0);
            clientePassedToGateway.setId(UUID.randomUUID());
            if (clientePassedToGateway.getEnderecos() != null) {
                clientePassedToGateway.getEnderecos().forEach(endereco -> {
                    endereco.setId(UUID.randomUUID());
                    assertNotNull(endereco.getUsuario());
                    assertEquals(clientePassedToGateway, endereco.getUsuario());
                });
            }
            return clientePassedToGateway;
        }).when(clienteGateway).cadastrarCliente(any(ClienteDomain.class));

        assertDoesNotThrow(() -> cadastrarClienteUseCase.execute(inputModel));

        verify(clienteGateway, times(1)).cadastrarCliente(any(ClienteDomain.class));
        verify(clienteOutputPort, times(1)).presentSuccess(any(ClienteDomain.class));
    }
}