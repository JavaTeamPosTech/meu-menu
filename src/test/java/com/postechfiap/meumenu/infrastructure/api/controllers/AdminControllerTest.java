package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.CadastrarAdminInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarAdminRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarAdminResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.BuscarTodosClientesPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.BuscarTodosProprietariosPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.admin.CadastrarAdminPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private BuscarTodosClientesAdminInputPort buscarTodosClientesAdminInputPort;

    @Mock
    private BuscarTodosProprietariosAdminInputPort buscarTodosProprietariosAdminInputPort;

    @Mock
    private BuscarTodosProprietariosPresenter buscarTodosProprietariosPresenter;

    @Mock
    private BuscarTodosClientesPresenter buscarTodosClientesPresenter;

    @Mock
    private CadastrarAdminInputPort cadastrarAdminInputPort;

    @Mock
    private CadastrarAdminPresenter cadastrarAdminPresenter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCadastrarAdmin() {
        CadastrarAdminRequestDTO requestDTO = mock(CadastrarAdminRequestDTO.class);
        CadastrarAdminResponseDTO responseDTO = mock(CadastrarAdminResponseDTO.class);

        when(cadastrarAdminPresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<CadastrarAdminResponseDTO> response = adminController.cadastrarAdmin(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(cadastrarAdminInputPort, times(1)).execute(requestDTO.toInputModel());
    }

    @Test
    void testBuscarTodosClientes_ComConteudo() {
        ClienteResponseDTO clienteResponseDTO = mock(ClienteResponseDTO.class);
        when(buscarTodosClientesPresenter.getViewModel()).thenReturn(List.of(clienteResponseDTO));
        when(buscarTodosClientesPresenter.isNoContent()).thenReturn(false);

        ResponseEntity<List<ClienteResponseDTO>> response = adminController.buscarTodosClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(buscarTodosClientesAdminInputPort, times(1)).execute();
    }

    @Test
    void testBuscarTodosClientes_SemConteudo() {
        when(buscarTodosClientesPresenter.getViewModel()).thenReturn(Collections.emptyList());
        when(buscarTodosClientesPresenter.isNoContent()).thenReturn(true);

        ResponseEntity<List<ClienteResponseDTO>> response = adminController.buscarTodosClientes();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(buscarTodosClientesAdminInputPort, times(1)).execute();
    }

    @Test
    void testBuscarTodosProprietarios_ComConteudo() {
        ProprietarioResponseDTO proprietarioResponseDTO = mock(ProprietarioResponseDTO.class);
        when(buscarTodosProprietariosPresenter.getViewModel()).thenReturn(List.of(proprietarioResponseDTO));
        when(buscarTodosProprietariosPresenter.isNoContent()).thenReturn(false);

        ResponseEntity<List<ProprietarioResponseDTO>> response = adminController.buscarTodosProprietarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(buscarTodosProprietariosAdminInputPort, times(1)).execute();
    }

    @Test
    void testBuscarTodosProprietarios_SemConteudo() {
        when(buscarTodosProprietariosPresenter.getViewModel()).thenReturn(Collections.emptyList());
        when(buscarTodosProprietariosPresenter.isNoContent()).thenReturn(true);

        ResponseEntity<List<ProprietarioResponseDTO>> response = adminController.buscarTodosProprietarios();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(buscarTodosProprietariosAdminInputPort, times(1)).execute();
    }
}