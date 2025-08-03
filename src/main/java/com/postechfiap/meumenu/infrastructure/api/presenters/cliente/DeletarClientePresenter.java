package com.postechfiap.meumenu.infrastructure.api.presenters.cliente;

import com.postechfiap.meumenu.core.domain.presenters.cliente.DeletarClienteOutputPort;
import com.postechfiap.meumenu.core.dtos.response.DeletarClienteResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class DeletarClientePresenter implements DeletarClienteOutputPort {

    private DeletarClienteResponseDTO viewModel;

    @Override
    public void presentSuccess(String message) {
        this.viewModel = new DeletarClienteResponseDTO(
                message,
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new DeletarClienteResponseDTO(
                message,
                "ERROR"
        );
    }

    @Override
    public boolean hasError() {
        return this.viewModel != null && "ERROR".equals(this.viewModel.status());
    }


    public DeletarClienteResponseDTO getViewModel() {
        return viewModel;
    }
}