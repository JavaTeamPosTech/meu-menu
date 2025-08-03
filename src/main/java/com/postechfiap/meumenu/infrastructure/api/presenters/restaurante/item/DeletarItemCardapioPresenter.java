package com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item;

import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.DeletarItemCardapioOutputPort;
import com.postechfiap.meumenu.dtos.response.MensagemResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class DeletarItemCardapioPresenter implements DeletarItemCardapioOutputPort {

    private MensagemResponseDTO viewModel;

    @Override
    public void presentSuccess(String message) {
        this.viewModel = new MensagemResponseDTO(
                message,
                "SUCCESS"
        );
    }

    public MensagemResponseDTO getViewModel() {
        return viewModel;
    }
}