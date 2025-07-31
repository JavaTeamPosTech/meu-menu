package com.postechfiap.meumenu.infrastructure.api.exceptions;

import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ErroValidacaoDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ExcecaoDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.CadastrarClientePresenter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final CadastrarClientePresenter cadastrarClientePresenter;
    // private final ConstraintViolationMessageResolver messageResolver;

    public GlobalExceptionHandler(CadastrarClientePresenter cadastrarClientePresenter /*, ConstraintViolationMessageResolver messageResolver */) {
        this.cadastrarClientePresenter = cadastrarClientePresenter;
        // this.messageResolver = messageResolver;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExcecaoDTO> handleBusinessException(BusinessException e) {
        cadastrarClientePresenter.presentError(e.getMessage());
        ExcecaoDTO response = new ExcecaoDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExcecaoDTO> handleAccessDeniedException(AccessDeniedException e) {
        ExcecaoDTO response = new ExcecaoDTO(
                "Acesso negado. Você não tem permissão para realizar esta operação.",
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

     @ExceptionHandler(AuthenticationException.class)
     public ResponseEntity<ExcecaoDTO> handleAuthenticationException(AuthenticationException e) {
         ExcecaoDTO response = new ExcecaoDTO(
                 "Credenciais inválidas ou acesso não autorizado.",
                 HttpStatus.UNAUTHORIZED.value(), // 401 Unauthorized
                 LocalDateTime.now(),
                 null
         );
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
     }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ExcecaoDTO> handlerResourceNotFoundException(ResourceNotFoundException e) {
//        ExcecaoDTO response = new ExcecaoDTO(
//                e.getMessage(),
//                HttpStatus.NOT_FOUND.value(),
//                LocalDateTime.now(),
//                null
//        );
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExcecaoDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErroValidacaoDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErroValidacaoDTO(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        ExcecaoDTO response = new ExcecaoDTO(
                "Dados inválidos.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExcecaoDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String detailMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
        ExcecaoDTO response = new ExcecaoDTO(
                "Violação de integridade de dados. Detalhes: " + detailMessage,
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExcecaoDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        ExcecaoDTO response = new ExcecaoDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExcecaoDTO> handleGenericException(Exception e, WebRequest request) {
        // logger.error("An unexpected error occurred: {}", e.getMessage(), e);

        ExcecaoDTO response = new ExcecaoDTO(
                "Ocorreu um erro interno inesperado. Por favor, tente novamente mais tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}