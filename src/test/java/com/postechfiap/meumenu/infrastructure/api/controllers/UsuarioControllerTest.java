package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.usuario.AlterarSenhaInputPort;
import com.postechfiap.meumenu.core.controllers.usuario.LoginInputPort;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AlterarSenhaRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.LoginRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.MensagemResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.usuario.AlterarSenhaPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.usuario.LoginPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private LoginInputPort loginInputPort;

    @Mock
    private LoginPresenter loginPresenter;

    @Mock
    private AlterarSenhaInputPort alterarSenhaInputPort;

    @Mock
    private AlterarSenhaPresenter alterarSenhaPresenter;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testLogin() {
        LoginRequestDTO requestDTO = mock(LoginRequestDTO.class);
        LoginResponseDTO responseDTO = mock(LoginResponseDTO.class);

        when(loginPresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = usuarioController.login(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(loginInputPort, times(1)).execute(requestDTO.toInputModel());
    }

    @Test
    void testAlterarSenha_SemUsuarioAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        AlterarSenhaRequestDTO requestDTO = mock(AlterarSenhaRequestDTO.class);

        BusinessException exception = assertThrows(BusinessException.class, () -> usuarioController.alterarSenha(requestDTO));

        assertEquals("Usuário não autenticado. Acesso negado.", exception.getMessage());
        verifyNoInteractions(alterarSenhaInputPort);
    }
}