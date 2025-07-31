package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.AdicionarItemCardapioOutputPort; // Importar a interface do Core
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ItemCardapioResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class AdicionarItemCardapioPresenter implements AdicionarItemCardapioOutputPort {

    private ItemCardapioResponseDTO viewModel;

    @Override
    public void presentSuccess(ItemCardapioDomain item) {
        this.viewModel = new ItemCardapioResponseDTO(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.getDisponivelApenasNoRestaurante(),
                item.getUrlFoto()
        );
    }

    public ItemCardapioResponseDTO getViewModel() {
        return viewModel;
    }
}