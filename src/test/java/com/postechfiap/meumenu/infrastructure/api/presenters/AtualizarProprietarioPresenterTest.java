package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarProprietarioPresenterTest {

    @InjectMocks
    private AtualizarProprietarioPresenter atualizarProprietarioPresenter;

    private ProprietarioDomain proprietarioDomain;
    private UUID proprietarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private EnderecoDomain enderecoDomain;
    private UUID enderecoId;
    private UsuarioDomain usuarioDomainParaEndereco;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        enderecoId = UUID.randomUUID();
        UUID usuarioIdParaEnderecoMock = UUID.randomUUID();
        dataCriacao = LocalDateTime.now().minusDays(10);
        dataAtualizacao = LocalDateTime.now();

        usuarioDomainParaEndereco = mock(UsuarioDomain.class);

        enderecoDomain = mock(EnderecoDomain.class);

        proprietarioDomain = mock(ProprietarioDomain.class);
        when(proprietarioDomain.getId()).thenReturn(proprietarioId);
        when(proprietarioDomain.getNome()).thenReturn("Dono do Restaurante");
        when(proprietarioDomain.getCpf()).thenReturn("11122233344");
        when(proprietarioDomain.getEmail()).thenReturn("dono@restaurante.com");
        when(proprietarioDomain.getLogin()).thenReturn("dono.login");
        when(proprietarioDomain.getWhatsapp()).thenReturn("11987654321");
        when(proprietarioDomain.getStatusConta()).thenReturn(StatusContaEnum.ATIVO);
        when(proprietarioDomain.getDataCriacao()).thenReturn(dataCriacao);
        when(proprietarioDomain.getDataAtualizacao()).thenReturn(dataAtualizacao);
        when(proprietarioDomain.getEnderecos()).thenReturn(new ArrayList<>(Collections.singletonList(enderecoDomain))); // Retorna lista com mock de endereço
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente com todos os dados")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        atualizarProprietarioPresenter.presentSuccess(proprietarioDomain);

        ProprietarioResponseDTO viewModel = atualizarProprietarioPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(proprietarioId, viewModel.id());
        assertEquals("Dono do Restaurante", viewModel.nome());
        assertEquals("11122233344", viewModel.cpf());
        assertEquals("dono@restaurante.com", viewModel.email());
        assertEquals("dono.login", viewModel.login());
        assertEquals("11987654321", viewModel.whatsapp());
        assertEquals(StatusContaEnum.ATIVO, viewModel.statusConta());
        assertEquals(dataCriacao, viewModel.dataCriacao());
        assertEquals(dataAtualizacao, viewModel.dataAtualizacao());

        assertNotNull(viewModel.enderecos());
        assertFalse(viewModel.enderecos().isEmpty());
        assertEquals(1, viewModel.enderecos().size());
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente com lista de endereços nula")
    void shouldPresentSuccessAndPopulateViewModelWithNullAddressList() {
        when(proprietarioDomain.getEnderecos()).thenReturn(null);

        atualizarProprietarioPresenter.presentSuccess(proprietarioDomain);

        ProprietarioResponseDTO viewModel = atualizarProprietarioPresenter.getViewModel();

        assertNotNull(viewModel);
        assertNull(viewModel.enderecos());
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente com lista de endereços vazia")
    void shouldPresentSuccessAndPopulateViewModelWithEmptyAddressList() {
        when(proprietarioDomain.getEnderecos()).thenReturn(Collections.emptyList());

        atualizarProprietarioPresenter.presentSuccess(proprietarioDomain);

        ProprietarioResponseDTO viewModel = atualizarProprietarioPresenter.getViewModel();

        assertNotNull(viewModel);
        assertNotNull(viewModel.enderecos());
        assertTrue(viewModel.enderecos().isEmpty());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        atualizarProprietarioPresenter.presentSuccess(proprietarioDomain);
        ProprietarioResponseDTO currentViewModel = atualizarProprietarioPresenter.getViewModel();

        assertNotNull(currentViewModel);
        assertEquals(proprietarioId, currentViewModel.id());
    }
}