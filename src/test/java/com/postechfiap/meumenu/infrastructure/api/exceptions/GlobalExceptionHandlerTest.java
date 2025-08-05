package com.postechfiap.meumenu.infrastructure.api.exceptions;

import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ErroValidacaoDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ExcecaoDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.CadastrarClientePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private CadastrarClientePresenter cadastrarClientePresenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler(cadastrarClientePresenter);
    }

    @Test
    void testHandleBusinessException() {
        BusinessException exception = new BusinessException("Erro de negócio");

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleBusinessException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro de negócio", response.getBody().getMensagem());
        verify(cadastrarClientePresenter, times(1)).presentError("Erro de negócio");
    }

    @Test
    void testHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleAccessDeniedException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acesso negado. Você não tem permissão para realizar esta operação.", response.getBody().getMensagem());
    }

    @Test
    void testHandleAuthenticationException() {
        AuthenticationException exception = mock(AuthenticationException.class);

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleAuthenticationException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Credenciais inválidas ou acesso não autorizado.", response.getBody().getMensagem());
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Violação de integridade");

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMensagem().contains("Violação de integridade"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Argumento inválido", response.getBody().getMensagem());
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Erro genérico");

        ResponseEntity<ExcecaoDTO> response = globalExceptionHandler.handleGenericException(exception, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde.", response.getBody().getMensagem());
    }
}