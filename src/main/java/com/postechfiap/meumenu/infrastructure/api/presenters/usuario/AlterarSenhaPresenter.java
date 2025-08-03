package com.postechfiap.meumenu.infrastructure.api.presenters.usuario;

import com.postechfiap.meumenu.core.domain.presenters.usuario.AlterarSenhaOutputPort;
import com.postechfiap.meumenu.core.dtos.response.MensagemResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class AlterarSenhaPresenter implements AlterarSenhaOutputPort {

    private MensagemResponseDTO viewModel;

    @Override
    public void presentSuccess(String message) {
        this.viewModel = new MensagemResponseDTO(
                message,
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new MensagemResponseDTO(
                message,
                "FAIL"
        );
    }

    public MensagemResponseDTO getViewModel() {
        return viewModel;
    }
}