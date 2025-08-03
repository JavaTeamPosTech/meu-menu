package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.presenters.proprietario.DeletarProprietarioOutputPort;
import com.postechfiap.meumenu.core.dtos.response.DeletarProprietarioResponseDTO;
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

    @Override
    public void presentError(String message) {
        this.viewModel = new DeletarProprietarioResponseDTO(
                message,
                "ERROR"
        );
    }

    @Override
    public boolean hasError() {
        return this.viewModel != null && "ERROR".equals(this.viewModel.status());
    }

    public DeletarProprietarioResponseDTO getViewModel() {
        return viewModel;
    }
}