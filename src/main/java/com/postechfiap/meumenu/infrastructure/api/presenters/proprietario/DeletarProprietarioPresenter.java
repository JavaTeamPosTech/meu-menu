package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.presenters.proprietario.DeletarProprietarioOutputPort;
import com.postechfiap.meumenu.dtos.response.DeletarProprietarioResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class DeletarProprietarioPresenter implements DeletarProprietarioOutputPort {

    private DeletarProprietarioResponseDTO viewModel;

    @Override
    public void presentSuccess(String message) {
        this.viewModel = new DeletarProprietarioResponseDTO(
                message,
                "SUCCESS"
        );
    }

    public DeletarProprietarioResponseDTO getViewModel() {
        return viewModel;
    }
}