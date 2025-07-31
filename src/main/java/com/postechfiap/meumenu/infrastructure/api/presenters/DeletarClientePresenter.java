package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.presenters.cliente.DeletarClienteOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.DeletarClienteResponseDTO;
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

    public DeletarClienteResponseDTO getViewModel() {
        return viewModel;
    }
}