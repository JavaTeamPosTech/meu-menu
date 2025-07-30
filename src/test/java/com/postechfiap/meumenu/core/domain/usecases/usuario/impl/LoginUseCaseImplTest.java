package com.postechfiap.meumenu.core.domain.usecases.usuario.impl;

import com.postechfiap.meumenu.core.domain.presenters.LoginOutputPort;
import com.postechfiap.meumenu.core.dtos.usuario.LoginInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.security.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // Para simular falha de autenticação
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private LoginOutputPort loginOutputPort;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    private LoginInputModel inputModel;
    private Authentication authenticationMock;
    private String generatedToken;

    @BeforeEach
    void setUp() {
        inputModel = new LoginInputModel("testuser", "password123");
        generatedToken = "mocked-jwt-token";

        // Mock Authentication object
        authenticationMock = mock(Authentication.class);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar o token JWT")
    void shouldLoginSuccessfullyAndReturnJwtToken() {
        // Mock: authenticationManager retorna um objeto Authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authenticationMock);
        // Mock: authenticationMock.getName retorna o login do usuário
        when(authenticationMock.getName()).thenReturn(inputModel.getLogin());
        // Mock: tokenService.generateToken retorna o token gerado
        when(tokenService.generateToken(anyString())).thenReturn(generatedToken);

        // Executa o Use Case
        String resultToken = assertDoesNotThrow(() -> loginUseCase.execute(inputModel));

        // Verifica o retorno
        assertNotNull(resultToken);
        assertEquals(generatedToken, resultToken);

        // Verifica as interações
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationMock, times(1)).getName(); // Verify getName was called on the Authentication object
        verify(tokenService, times(1)).generateToken(inputModel.getLogin());
        verify(loginOutputPort, times(1)).presentSuccess(generatedToken);
        verify(loginOutputPort, never()).presentError(anyString()); // presentError não deve ser chamado
    }

    @Test
    @DisplayName("Deve lançar BusinessException e notificar erro do Presenter se as credenciais forem inválidas")
    void shouldThrowBusinessExceptionAndPresentErrorWhenCredentialsInvalid() {
        // Mock: authenticationManager lança AuthenticationException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials")); // Simula falha

        // Executa o Use Case e verifica a exceção
        BusinessException exception = assertThrows(BusinessException.class,
                () -> loginUseCase.execute(inputModel));

        // Verifica a mensagem da exceção
        assertEquals("Credenciais inválidas.", exception.getMessage());

        // Verifica as interações
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generateToken(anyString()); // generateToken NÃO deve ser chamado
        verify(loginOutputPort, times(1)).presentError("Credenciais inválidas: Bad credentials"); // presentError deve ser chamado
        verify(loginOutputPort, never()).presentSuccess(anyString()); // presentSuccess não deve ser chamado
    }
}