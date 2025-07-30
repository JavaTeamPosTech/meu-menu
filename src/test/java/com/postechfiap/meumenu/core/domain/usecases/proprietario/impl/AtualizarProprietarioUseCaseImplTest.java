package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarProprietarioOutputPort;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarProprietarioUseCaseImplTest {

    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private AtualizarProprietarioOutputPort atualizarProprietarioOutputPort;

    @InjectMocks
    private AtualizarProprietarioUseCaseImpl atualizarProprietarioUseCase;

    private UUID proprietarioId;
    private ProprietarioDomain proprietarioExistente;
    private AtualizarProprietarioInputModel inputModel;
    private ProprietarioDomain proprietarioAtualizadoRetornado;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();

        proprietarioExistente = new ProprietarioDomain(
                proprietarioId,
                "11122233344",
                "11988887777",
                StatusContaEnum.ATIVO,
                "Proprietario Original",
                "proprietario@dev.com",
                "proprietario",
                "senha_hash_original",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(10),
                Collections.emptyList()
        );

        inputModel = new AtualizarProprietarioInputModel(
                proprietarioId, // ID do proprietário a ser atualizado
                "Proprietario Atualizado", // Nome
                "atualizado_prop@email.com", // Email
                "login_atualizado_prop", // Login
                "99988877766", // CPF
                "11977776666", // Whatsapp
                StatusContaEnum.BLOQUEADO // Status Conta
        );

        proprietarioAtualizadoRetornado = new ProprietarioDomain(
                proprietarioId,
                inputModel.getCpf(),
                inputModel.getWhatsapp(),
                inputModel.getStatusConta(),
                inputModel.getNome(),
                inputModel.getEmail(),
                inputModel.getLogin(),
                "senha_hash_original",
                proprietarioExistente.getDataCriacao(),
                LocalDateTime.now(),
                Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Deve atualizar um proprietário com sucesso sem conflitos")
    void shouldUpdateProprietarioSuccessfullyWithoutConflicts() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false);
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(false);
        when(proprietarioGateway.existsByCpf(inputModel.getCpf())).thenReturn(false);

        when(proprietarioGateway.atualizarProprietario(any(ProprietarioDomain.class))).thenReturn(proprietarioAtualizadoRetornado);

        ProprietarioDomain result = assertDoesNotThrow(() -> atualizarProprietarioUseCase.execute(inputModel));

        assertNotNull(result);
        assertEquals(inputModel.getNome(), result.getNome());
        assertEquals(inputModel.getEmail(), result.getEmail());
        assertEquals(inputModel.getLogin(), result.getLogin());
        assertEquals(inputModel.getCpf(), result.getCpf());
        assertEquals(inputModel.getWhatsapp(), result.getWhatsapp());
        assertEquals(inputModel.getStatusConta(), result.getStatusConta());
        assertNotNull(result.getDataAtualizacao());

        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(proprietarioGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(proprietarioGateway, times(1)).atualizarProprietario(any(ProprietarioDomain.class)); // Verifica se o proprietário foi passado para atualização
        verify(atualizarProprietarioOutputPort, times(1)).presentSuccess(any(ProprietarioDomain.class)); // Verifica notificação de sucesso
    }

    @Test
    @DisplayName("Deve atualizar proprietário com sucesso se campos não foram alterados")
    void shouldUpdateProprietarioSuccessfullyIfFieldsAreNotChanged() {
        inputModel = new AtualizarProprietarioInputModel(
                proprietarioId, proprietarioExistente.getNome(), proprietarioExistente.getEmail(), proprietarioExistente.getLogin(),
                proprietarioExistente.getCpf(), proprietarioExistente.getWhatsapp(), proprietarioExistente.getStatusConta()
        );

        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioExistente));
        when(proprietarioGateway.atualizarProprietario(any(ProprietarioDomain.class))).thenReturn(proprietarioAtualizadoRetornado);

        assertDoesNotThrow(() -> atualizarProprietarioUseCase.execute(inputModel));

        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, never()).existsByEmail(anyString()); // Não deve verificar email
        verify(usuarioGateway, never()).existsByLogin(anyString()); // Não deve verificar login
        verify(proprietarioGateway, never()).existsByCpf(anyString()); // Não deve verificar CPF
        verify(proprietarioGateway, times(1)).atualizarProprietario(any(ProprietarioDomain.class));
        verify(atualizarProprietarioOutputPort, times(1)).presentSuccess(any(ProprietarioDomain.class));
    }


    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o proprietário não for encontrado para atualização")
    void shouldThrowResourceNotFoundExceptionWhenProprietarioNotFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> atualizarProprietarioUseCase.execute(inputModel));

        assertEquals("Proprietário com ID " + proprietarioId + " não encontrado para atualização.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, never()).existsByEmail(anyString());
        verify(usuarioGateway, never()).existsByLogin(anyString());
        verify(proprietarioGateway, never()).existsByCpf(anyString());
        verify(proprietarioGateway, never()).atualizarProprietario(any(ProprietarioDomain.class));
        verify(atualizarProprietarioOutputPort, never()).presentSuccess(any(ProprietarioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o email já existir para outro usuário")
    void shouldThrowBusinessExceptionWhenEmailAlreadyExists() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(true); // Email já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarProprietarioUseCase.execute(inputModel));

        assertEquals("Email '" + inputModel.getEmail() + "' já cadastrado por outro usuário.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, never()).existsByLogin(anyString());
        verify(proprietarioGateway, never()).existsByCpf(anyString());
        verify(proprietarioGateway, never()).atualizarProprietario(any(ProprietarioDomain.class));
        verify(atualizarProprietarioOutputPort, never()).presentSuccess(any(ProprietarioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o login já existir para outro usuário")
    void shouldThrowBusinessExceptionWhenLoginAlreadyExists() {
        // Mock
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false); // Email não existe (passa)
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(true); // Login já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarProprietarioUseCase.execute(inputModel));

        assertEquals("Login '" + inputModel.getLogin() + "' já cadastrado por outro usuário.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(proprietarioGateway, never()).existsByCpf(anyString());
        verify(proprietarioGateway, never()).atualizarProprietario(any(ProprietarioDomain.class));
        verify(atualizarProprietarioOutputPort, never()).presentSuccess(any(ProprietarioDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o CPF já existir para outro proprietário")
    void shouldThrowBusinessExceptionWhenCpfAlreadyExists() {
        // Mock
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioExistente));
        when(usuarioGateway.existsByEmail(inputModel.getEmail())).thenReturn(false);
        when(usuarioGateway.existsByLogin(inputModel.getLogin())).thenReturn(false);
        when(proprietarioGateway.existsByCpf(inputModel.getCpf())).thenReturn(true); // CPF já existe

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarProprietarioUseCase.execute(inputModel));

        assertEquals("CPF '" + inputModel.getCpf() + "' já cadastrado por outro proprietário.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(usuarioGateway, times(1)).existsByEmail(inputModel.getEmail());
        verify(usuarioGateway, times(1)).existsByLogin(inputModel.getLogin());
        verify(proprietarioGateway, times(1)).existsByCpf(inputModel.getCpf());
        verify(proprietarioGateway, never()).atualizarProprietario(any(ProprietarioDomain.class));
        verify(atualizarProprietarioOutputPort, never()).presentSuccess(any(ProprietarioDomain.class));
    }
}