package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ItemCardapioResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarItemCardapioPresenterTest {

    @InjectMocks
    private AtualizarItemCardapioPresenter atualizarItemCardapioPresenter;

    private ItemCardapioDomain itemCardapioDomain;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();
        itemCardapioDomain = mock(ItemCardapioDomain.class);
        when(itemCardapioDomain.getId()).thenReturn(itemId);
        when(itemCardapioDomain.getNome()).thenReturn("Lasanha Bolonhesa");
        when(itemCardapioDomain.getDescricao()).thenReturn("Deliciosa lasanha com molho de carne e queijo.");
        when(itemCardapioDomain.getPreco()).thenReturn(BigDecimal.valueOf(65.50));
        when(itemCardapioDomain.getDisponivelApenasNoRestaurante()).thenReturn(true);
        when(itemCardapioDomain.getUrlFoto()).thenReturn("http://cardapio.com/lasanha.jpg");
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente com todos os campos")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        atualizarItemCardapioPresenter.presentSuccess(itemCardapioDomain);

        ItemCardapioResponseDTO viewModel = atualizarItemCardapioPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(itemId, viewModel.id());
        assertEquals("Lasanha Bolonhesa", viewModel.nome());
        assertEquals("Deliciosa lasanha com molho de carne e queijo.", viewModel.descricao());
        assertEquals(BigDecimal.valueOf(65.50), viewModel.preco());
        assertEquals(true, viewModel.disponivelApenasNoRestaurante());
        assertEquals("http://cardapio.com/lasanha.jpg", viewModel.urlFoto());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        atualizarItemCardapioPresenter.presentSuccess(itemCardapioDomain);
        ItemCardapioResponseDTO currentViewModel = atualizarItemCardapioPresenter.getViewModel();

        assertNotNull(currentViewModel);
        assertEquals(itemCardapioDomain.getNome(), currentViewModel.nome());
        assertEquals(itemCardapioDomain.getId(), currentViewModel.id());
    }
}