package com.postechfiap.meumenu.infrastructure.api.presenters.admin;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarAdminResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CadastrarAdminPresenterTest {

    private CadastrarAdminPresenter cadastrarAdminPresenter;

    @BeforeEach
    void setUp() {
        cadastrarAdminPresenter = new CadastrarAdminPresenter();
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        UUID adminId = UUID.randomUUID();
        LocalDateTime dataCriacao = LocalDateTime.now();
        AdminDomain adminDomain = new AdminDomain(
                adminId,
                "Admin Teste",
                "admin@email.com",
                "admin_login",
                "password123",
                dataCriacao,
                dataCriacao,
                new ArrayList<>()
        );

        cadastrarAdminPresenter.presentSuccess(adminDomain);

        CadastrarAdminResponseDTO viewModel = cadastrarAdminPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(adminId, viewModel.id());
        assertEquals("Admin Teste", viewModel.nome());
        assertEquals("admin@email.com", viewModel.email());
        assertEquals("admin_login", viewModel.login());
        assertEquals(dataCriacao, viewModel.dataCriacao());
        assertEquals("Admin cadastrado com sucesso!", viewModel.message());
        assertEquals("SUCCESS", viewModel.status());
    }

    @Test
    @DisplayName("Deve apresentar erro e popular o ViewModel corretamente")
    void shouldPresentErrorAndPopulateViewModelCorrectly() {
        String errorMessage = "Erro ao cadastrar admin";

        cadastrarAdminPresenter.presentError(errorMessage);

        CadastrarAdminResponseDTO viewModel = cadastrarAdminPresenter.getViewModel();

        assertNotNull(viewModel);
        assertNull(viewModel.id());
        assertNull(viewModel.nome());
        assertNull(viewModel.email());
        assertNull(viewModel.login());
        assertNull(viewModel.dataCriacao());
        assertEquals(errorMessage, viewModel.message());
        assertEquals("FAIL", viewModel.status());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        UUID adminId = UUID.randomUUID();
        LocalDateTime dataCriacao = LocalDateTime.now();
        AdminDomain adminDomain = new AdminDomain(
                adminId,
                "Admin Teste",
                "admin@email.com",
                "admin_login",
                "password123",
                dataCriacao,
                dataCriacao,
                new ArrayList<>()
        );

        cadastrarAdminPresenter.presentSuccess(adminDomain);

        CadastrarAdminResponseDTO currentViewModel = cadastrarAdminPresenter.getViewModel();

        assertNotNull(currentViewModel);
        assertEquals(adminId, currentViewModel.id());
        assertEquals("Admin Teste", currentViewModel.nome());
    }
}