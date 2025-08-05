package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.proprietario.AtualizarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarProprietarioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarProprietarioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.DeletarProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.AtualizarProprietarioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.BuscarProprietarioPorIdPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.CadastrarProprietarioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.DeletarProprietarioPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProprietarioControllerTest {

    @Mock
    private CadastrarProprietarioInputPort cadastrarInputPort;
    @Mock
    private CadastrarProprietarioPresenter cadastrarPresenter;

    @Mock
    private BuscarProprietarioPorIdInputPort buscarInputPort;
    @Mock
    private BuscarProprietarioPorIdPresenter buscarPresenter;

    @Mock
    private DeletarProprietarioInputPort deletarInputPort;
    @Mock
    private DeletarProprietarioPresenter deletarPresenter;

    @Mock
    private AtualizarProprietarioInputPort atualizarInputPort;
    @Mock
    private AtualizarProprietarioPresenter atualizarPresenter;

    @InjectMocks
    private ProprietarioController controller;

    private UUID proprietarioId;

    @BeforeEach
    void setup() {
        proprietarioId = UUID.randomUUID();
    }

    @Test
    void deveCadastrarProprietarioComSucesso() {
        var request = mock(CadastrarProprietarioRequestDTO.class);
        var response = mock(CadastrarProprietarioResponseDTO.class);

        when(cadastrarPresenter.getViewModel()).thenReturn(response);

        ResponseEntity<CadastrarProprietarioResponseDTO> resultado = controller.cadastrarProprietario(request);

        verify(cadastrarPresenter).getViewModel();

        assertEquals(201, resultado.getStatusCodeValue());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void deveBuscarProprietarioPorIdComSucesso() {
        ProprietarioResponseDTO response = mock(ProprietarioResponseDTO.class);
        when(buscarPresenter.getViewModel()).thenReturn(response);

        ResponseEntity<ProprietarioResponseDTO> resultado = controller.buscarProprietarioPorId(proprietarioId);

        verify(buscarInputPort).execute(proprietarioId);
        verify(buscarPresenter).getViewModel();

        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void deveDeletarProprietarioComSucesso() {
        DeletarProprietarioResponseDTO response = mock(DeletarProprietarioResponseDTO.class);
        when(deletarPresenter.getViewModel()).thenReturn(response);

        ResponseEntity<DeletarProprietarioResponseDTO> resultado = controller.deletarProprietario(proprietarioId);

        verify(deletarInputPort).execute(proprietarioId);
        verify(deletarPresenter).getViewModel();

        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(response, resultado.getBody());
    }

    @Test
    void deveAtualizarProprietarioComSucesso() {
        AtualizarProprietarioRequestDTO request = mock(AtualizarProprietarioRequestDTO.class);
        ProprietarioResponseDTO response = mock(ProprietarioResponseDTO.class);

        when(atualizarPresenter.getViewModel()).thenReturn(response);

        ResponseEntity<ProprietarioResponseDTO> resultado = controller.atualizarProprietario(proprietarioId, request);

        verify(atualizarPresenter).getViewModel();

        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(response, resultado.getBody());
    }
}