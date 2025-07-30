package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.CadastrarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import com.postechfiap.meumenu.core.dtos.endereco.EnderecoInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarProprietarioUseCaseImplTest {

    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private PasswordService passwordService;
    @Mock
    private CadastrarProprietarioOutputPort cadastrarProprietarioOutputPort;

    @InjectMocks
    private CadastrarProprietarioUseCaseImpl cadastrarProprietarioUseCase;

    private CadastrarProprietarioInputModel inputModel;
    private ProprietarioDomain novoProprietarioMock;
    private EnderecoInputModel enderecoInputModel;

    @BeforeEach
    void setUp() {
        enderecoInputModel = new EnderecoInputModel(
                "SP", "Sao Paulo", "Centro", "Rua Y", 456, "Sala 10", "02000-000");

        inputModel = new CadastrarProprietarioInputModel(
                "Proprietario Teste", "prop@email.com", "proplogin", "senha_prop123",
                "00011122233", // CPF
                "11988887777", // Whatsapp
                List.of(enderecoInputModel)
        );

        novoProprietarioMock = new ProprietarioDomain(
                inputModel.getNome(),
                inputModel.getEmail(),
                inputModel.getLogin(),
                "senha_prop_hash",
                inputModel.getCpf(),
                inputModel.getWhatsapp()
        );
        novoProprietarioMock.setId(UUID.randomUUID());
        novoProprietarioMock.setDataCriacao(LocalDateTime.now());
        novoProprietarioMock.setDataAtualizacao(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve cadastrar um novo proprietário com sucesso")
    void shouldCadastrarProprietarioSuccessfully() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(passwordService.encryptPassword(anyString())).thenReturn("senha_prop_hash");

        when(proprietarioGateway.cadastrarProprietario(any(ProprietarioDomain.class)))
                .thenAnswer(invocation -> {
                    ProprietarioDomain proprietarioPassed = invocation.getArgument(0);
                    proprietarioPassed.setId(UUID.randomUUID());
                    if (proprietarioPassed.getEnderecos() != null) {
                        proprietarioPassed.getEnderecos().forEach(endereco -> {
                            endereco.setId(UUID.randomUUID());
                            assertNotNull(endereco.getUsuario());
                            assertEquals(proprietarioPassed, endereco.getUsuario());
                        });
                    }
                    return proprietarioPassed;
                });

        assertDoesNotThrow(() -> cadastrarProprietarioUseCase.execute(inputModel));

        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(passwordService, times(1)).encryptPassword(inputModel.getSenha());
        verify(proprietarioGateway, times(1)).cadastrarProprietario(any(ProprietarioDomain.class));
        verify(cadastrarProprietarioOutputPort, times(1)).presentSuccess(any(ProprietarioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se login de proprietário já existir")
    void shouldThrowBusinessExceptionWhenProprietarioLoginExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarProprietarioUseCase.execute(inputModel));

        assertEquals("Login já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, never()).existsByEmail(anyString());
        verify(proprietarioGateway, never()).existsByCpf(anyString());
        verify(cadastrarProprietarioOutputPort, times(1)).presentError("Login já cadastrado.");
    }

    @Test
    @DisplayName("Deve lançar BusinessException se email de proprietário já existir")
    void shouldThrowBusinessExceptionWhenProprietarioEmailExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarProprietarioUseCase.execute(inputModel));

        assertEquals("Email já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(proprietarioGateway, never()).existsByCpf(anyString());
        verify(cadastrarProprietarioOutputPort, times(1)).presentError("Email já cadastrado.");
    }

    @Test
    @DisplayName("Deve lançar BusinessException se CPF de proprietário já existir")
    void shouldThrowBusinessExceptionWhenProprietarioCpfExists() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(proprietarioGateway.existsByCpf(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarProprietarioUseCase.execute(inputModel));

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(proprietarioGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(cadastrarProprietarioOutputPort, times(1)).presentError("CPF já cadastrado.");
    }

    @Test
    @DisplayName("Deve associar EnderecoDomain ao ProprietarioDomain e ao UsuarioDomain pai")
    void shouldAssociateEnderecoDomainToProprietarioDomainCorrectly() {
        when(usuarioGateway.existsByLogin(anyString())).thenReturn(false);
        when(usuarioGateway.existsByEmail(anyString())).thenReturn(false);
        when(passwordService.encryptPassword(anyString())).thenReturn("senha_prop_hash");

        doAnswer(invocation -> {
            ProprietarioDomain proprietarioPassedToGateway = invocation.getArgument(0);
            proprietarioPassedToGateway.setId(UUID.randomUUID());
            if (proprietarioPassedToGateway.getEnderecos() != null) {
                proprietarioPassedToGateway.getEnderecos().forEach(endereco -> {
                    endereco.setId(UUID.randomUUID());
                    assertNotNull(endereco.getUsuario());
                    assertEquals(proprietarioPassedToGateway, endereco.getUsuario());
                });
            }
            return proprietarioPassedToGateway;
        }).when(proprietarioGateway).cadastrarProprietario(any(ProprietarioDomain.class));

        assertDoesNotThrow(() -> cadastrarProprietarioUseCase.execute(inputModel));

        verify(proprietarioGateway, times(1)).cadastrarProprietario(any(ProprietarioDomain.class));
        verify(cadastrarProprietarioOutputPort, times(1)).presentSuccess(any(ProprietarioDomain.class));
    }
}