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
class AdicionarItemCardapioPresenterTest {

    @InjectMocks
    private AdicionarItemCardapioPresenter adicionarItemCardapioPresenter;

    private ItemCardapioDomain itemCardapioDomain;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();
        itemCardapioDomain = mock(ItemCardapioDomain.class);
        when(itemCardapioDomain.getId()).thenReturn(itemId);
        when(itemCardapioDomain.getNome()).thenReturn("Sanduíche Clássico");
        when(itemCardapioDomain.getDescricao()).thenReturn("Pão, bife, queijo, alface, tomate.");
        when(itemCardapioDomain.getPreco()).thenReturn(BigDecimal.valueOf(25.99));
        when(itemCardapioDomain.getDisponivelApenasNoRestaurante()).thenReturn(false);
        when(itemCardapioDomain.getUrlFoto()).thenReturn("http://foto.com/sanduiche.jpg");
    }

    @Test
    @DisplayName("Deve apresentar sucesso e popular o ViewModel corretamente")
    void shouldPresentSuccessAndPopulateViewModelCorrectly() {
        adicionarItemCardapioPresenter.presentSuccess(itemCardapioDomain);

        ItemCardapioResponseDTO viewModel = adicionarItemCardapioPresenter.getViewModel();

        assertNotNull(viewModel);
        assertEquals(itemId, viewModel.id());
        assertEquals("Sanduíche Clássico", viewModel.nome());
        assertEquals("Pão, bife, queijo, alface, tomate.", viewModel.descricao());
        assertEquals(BigDecimal.valueOf(25.99), viewModel.preco());
        assertEquals(false, viewModel.disponivelApenasNoRestaurante());
        assertEquals("http://foto.com/sanduiche.jpg", viewModel.urlFoto());
    }

    @Test
    @DisplayName("GetViewModel deve retornar o ViewModel atual")
    void getViewModel_shouldReturnCurrentViewModel() {
        adicionarItemCardapioPresenter.presentSuccess(itemCardapioDomain);
        ItemCardapioResponseDTO currentViewModel = adicionarItemCardapioPresenter.getViewModel();

        assertNotNull(currentViewModel);
        assertEquals(itemCardapioDomain.getNome(), currentViewModel.nome());
    }
}