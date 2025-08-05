package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.restaurante.*;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.*;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.*;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteControllerTest {

    @InjectMocks
    private RestauranteController restauranteController;

    @Mock
    private CadastrarRestauranteInputPort cadastrarRestauranteInputPort;

    @Mock
    private CadastrarRestaurantePresenter cadastrarRestaurantePresenter;

    @Mock
    private BuscarTodosRestaurantesInputPort buscarTodosRestaurantesInputPort;

    @Mock
    private BuscarTodosRestaurantesPresenter buscarTodosRestaurantesPresenter;

    @Mock
    private BuscarRestaurantePorIdInputPort buscarRestaurantePorIdInputPort;

    @Mock
    private BuscarRestaurantePorIdPresenter buscarRestaurantePorIdPresenter;

    @Mock
    private AtualizarRestauranteInputPort atualizarRestauranteInputPort;

    @Mock
    private AtualizarRestaurantePresenter atualizarRestaurantePresenter;

    @Mock
    private DeletarRestauranteInputPort deletarRestauranteInputPort;

    @Mock
    private DeletarRestaurantePresenter deletarRestaurantePresenter;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCadastrarRestaurante() {
        CadastrarRestauranteRequestDTO requestDTO = mock(CadastrarRestauranteRequestDTO.class);
        CadastrarRestauranteResponseDTO responseDTO = mock(CadastrarRestauranteResponseDTO.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        ProprietarioEntity proprietario = new ProprietarioEntity();
        proprietario.setId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(proprietario);

        when(cadastrarRestaurantePresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<CadastrarRestauranteResponseDTO> response = restauranteController.cadastrarRestaurante(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(cadastrarRestauranteInputPort, times(1)).execute(any());
    }

    @Test
    void testBuscarTodosRestaurantes_ComConteudo() {
        RestauranteResponseDTO restauranteResponseDTO = mock(RestauranteResponseDTO.class);
        when(buscarTodosRestaurantesPresenter.getViewModel()).thenReturn(List.of(restauranteResponseDTO));
        when(buscarTodosRestaurantesPresenter.isNoContent()).thenReturn(false);

        ResponseEntity<List<RestauranteResponseDTO>> response = restauranteController.buscarTodosRestaurantes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(buscarTodosRestaurantesInputPort, times(1)).execute();
    }

    @Test
    void testBuscarTodosRestaurantes_SemConteudo() {
        when(buscarTodosRestaurantesPresenter.getViewModel()).thenReturn(Collections.emptyList());
        when(buscarTodosRestaurantesPresenter.isNoContent()).thenReturn(true);

        ResponseEntity<List<RestauranteResponseDTO>> response = restauranteController.buscarTodosRestaurantes();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(buscarTodosRestaurantesInputPort, times(1)).execute();
    }

    @Test
    void testBuscarRestaurantePorId() {
        UUID id = UUID.randomUUID();
        RestauranteResponseDTO responseDTO = mock(RestauranteResponseDTO.class);

        when(buscarRestaurantePorIdPresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<RestauranteResponseDTO> response = restauranteController.buscarRestaurantePorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(buscarRestaurantePorIdInputPort, times(1)).execute(id);
    }

    @Test
    void testAtualizarRestaurante() {
        UUID id = UUID.randomUUID();
        AtualizarRestauranteRequestDTO requestDTO = mock(AtualizarRestauranteRequestDTO.class);
        RestauranteResponseDTO responseDTO = mock(RestauranteResponseDTO.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        ProprietarioEntity proprietario = new ProprietarioEntity();
        proprietario.setId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(proprietario);

        when(atualizarRestaurantePresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<RestauranteResponseDTO> response = restauranteController.atualizarRestaurante(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(atualizarRestauranteInputPort, times(1)).execute(eq(id), any());
    }

    @Test
    void testDeletarRestaurante() {
        UUID id = UUID.randomUUID();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        ProprietarioEntity proprietario = new ProprietarioEntity();
        proprietario.setId(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(proprietario);

        ResponseEntity<Void> response = restauranteController.deletarRestaurante(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deletarRestauranteInputPort, times(1)).execute(eq(id), any());
    }
}