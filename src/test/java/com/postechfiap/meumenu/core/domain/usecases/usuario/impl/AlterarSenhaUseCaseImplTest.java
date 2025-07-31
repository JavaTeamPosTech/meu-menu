package com.postechfiap.meumenu.core.domain.usecases.usuario.impl;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.usuario.AlterarSenhaOutputPort;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaUseCaseImplTest {

    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private PasswordService passwordService;
    @Mock
    private AlterarSenhaOutputPort alterarSenhaOutputPort;

    @InjectMocks
    private AlterarSenhaUseCaseImpl alterarSenhaUseCase;

    private UUID usuarioId;
    private UsuarioDomain usuarioExistente;
    private AlterarSenhaInputModel inputModel;
    private String senhaAntigaHash;
    private String novaSenhaHash;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.randomUUID();
        senhaAntigaHash = "old_password_hash";
        novaSenhaHash = "new_password_hash";

        // Usuário existente (simula como viria do banco)
        usuarioExistente = new UsuarioDomain(
                usuarioId, "Usuario Teste", "user@test.com", "usertest", senhaAntigaHash,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5), Collections.emptyList()
        );

        // Input Model para alteração de senha
        inputModel = new AlterarSenhaInputModel(usuarioId, "old_password_raw", "new_password_raw");
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso")
    void shouldChangePasswordSuccessfully() {
        // Mocks: usuarioGateway.buscarUsuarioPorId retorna o usuário existente
        when(usuarioGateway.buscarUsuarioPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        // Mocks: passwordService.matches retorna true para a senha antiga
        when(passwordService.matches(inputModel.getSenhaAntiga(), usuarioExistente.getSenha())).thenReturn(true);
        // Mocks: passwordService.encryptPassword retorna a nova senha hash
        when(passwordService.encryptPassword(inputModel.getNovaSenha())).thenReturn(novaSenhaHash);
        // Mocks: usuarioGateway.atualizarUsuario retorna o usuário atualizado (ou o próprio existente se for o mesmo objeto)
        when(usuarioGateway.atualizarUsuario(any(UsuarioDomain.class))).thenReturn(usuarioExistente); // Retornamos o existente, que terá a senha atualizada

        // Executa o Use Case
        assertDoesNotThrow(() -> alterarSenhaUseCase.execute(inputModel));

        // Verifica o estado da senha no objeto que seria passado para o gateway
        assertEquals(novaSenhaHash, usuarioExistente.getSenha()); // A senha no objeto deve ter sido atualizada

        // Verifica as interações
        verify(usuarioGateway, times(1)).buscarUsuarioPorId(usuarioId);
        verify(passwordService, times(1)).encryptPassword(inputModel.getNovaSenha());
        verify(usuarioGateway, times(1)).atualizarUsuario(usuarioExistente); // Verifica a chamada com o objeto atualizado
        verify(alterarSenhaOutputPort, times(1)).presentSuccess("Senha alterada com sucesso para o usuário ID " + usuarioId + ".");
        verify(alterarSenhaOutputPort, never()).presentError(anyString());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o usuário não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
        // Mock: usuarioGateway.buscarUsuarioPorId retorna Optional.empty
        when(usuarioGateway.buscarUsuarioPorId(usuarioId)).thenReturn(Optional.empty());

        // Executa o Use Case e verifica a exceção
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> alterarSenhaUseCase.execute(inputModel));

        assertEquals("Usuário com ID " + usuarioId + " não encontrado para alteração de senha.", exception.getMessage());
        verify(usuarioGateway, times(1)).buscarUsuarioPorId(usuarioId);
        verify(passwordService, never()).matches(anyString(), anyString()); // Não deve verificar senha
        verify(passwordService, never()).encryptPassword(anyString()); // Não deve criptografar
        verify(usuarioGateway, never()).atualizarUsuario(any(UsuarioDomain.class)); // Não deve atualizar
        verify(alterarSenhaOutputPort, never()).presentSuccess(anyString());
        verify(alterarSenhaOutputPort, never()).presentError(anyString());
    }

    @Test
    @DisplayName("Deve lançar BusinessException se a senha antiga estiver incorreta")
    void shouldThrowBusinessExceptionWhenOldPasswordIsIncorrect() {
        // Mock: usuarioGateway.buscarUsuarioPorId retorna o usuário existente
        when(usuarioGateway.buscarUsuarioPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        // Mock: passwordService.matches retorna false (senha incorreta)
        when(passwordService.matches(inputModel.getSenhaAntiga(), usuarioExistente.getSenha())).thenReturn(false);

        // Executa o Use Case e verifica a exceção
        BusinessException exception = assertThrows(BusinessException.class,
                () -> alterarSenhaUseCase.execute(inputModel));

        assertEquals("Senha antiga incorreta.", exception.getMessage());
        verify(usuarioGateway, times(1)).buscarUsuarioPorId(usuarioId);
        verify(passwordService, times(1)).matches(inputModel.getSenhaAntiga(), usuarioExistente.getSenha());
        verify(passwordService, never()).encryptPassword(anyString());
        verify(usuarioGateway, never()).atualizarUsuario(any(UsuarioDomain.class));
        verify(alterarSenhaOutputPort, never()).presentSuccess(anyString());
        verify(alterarSenhaOutputPort, times(1)).presentError("Senha antiga incorreta."); // Notifica erro
    }
}