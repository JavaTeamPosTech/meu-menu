package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.cliente.*;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarClienteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarClienteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.DeletarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private CadastrarClienteInputPort cadastrarClienteInputPort;

    @Mock
    private CadastrarClientePresenter cadastrarClientePresenter;

    @Mock
    private BuscarClientePorIdInputPort buscarClientePorIdInputPort;

    @Mock
    private BuscarClientePorIdPresenter buscarClientePorIdPresenter;

    @Mock
    private DeletarClienteInputPort deletarClienteInputPort;

    @Mock
    private DeletarClientePresenter deletarClientePresenter;

    @Mock
    private AtualizarClienteInputPort atualizarClienteInputPort;

    @Mock
    private AtualizarClientePresenter atualizarClientePresenter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCadastrarCliente() {
        CadastrarClienteRequestDTO requestDTO = mock(CadastrarClienteRequestDTO.class);
        CadastrarClienteResponseDTO responseDTO = mock(CadastrarClienteResponseDTO.class);
        when(cadastrarClientePresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<CadastrarClienteResponseDTO> response = clienteController.cadastrarCliente(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(cadastrarClienteInputPort, times(1)).execute(requestDTO.toInputModel());
    }

    @Test
    void testBuscarClientePorId_ClienteEncontrado() {
        UUID id = UUID.randomUUID();
        ClienteDomain clienteDomain = new ClienteDomain();
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);

        when(buscarClientePorIdInputPort.execute(id)).thenReturn(Optional.of(clienteDomain));
        when(buscarClientePorIdPresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<ClienteResponseDTO> response = clienteController.buscarClientePorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(buscarClientePorIdInputPort, times(1)).execute(id);
    }

    @Test
    void testBuscarClientePorId_ClienteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);

        when(buscarClientePorIdInputPort.execute(id)).thenReturn(Optional.empty());
        when(buscarClientePorIdPresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<ClienteResponseDTO> response = clienteController.buscarClientePorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(buscarClientePorIdInputPort, times(1)).execute(id);
    }

    @Test
    void testDeletarCliente() {
        UUID id = UUID.randomUUID();
        DeletarClienteResponseDTO responseDTO = mock(DeletarClienteResponseDTO.class);

        when(deletarClientePresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<DeletarClienteResponseDTO> response = clienteController.deletarCliente(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(deletarClienteInputPort, times(1)).execute(id);
    }

    @Test
    void testAtualizarCliente() {
        UUID id = UUID.randomUUID();
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);
        AtualizarClienteRequestDTO requestDTO = mock(AtualizarClienteRequestDTO.class);

        when(atualizarClientePresenter.getViewModel()).thenReturn(responseDTO);

        ResponseEntity<ClienteResponseDTO> response = clienteController.atualizarCliente(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(atualizarClienteInputPort, times(1)).execute(any());
    }
}