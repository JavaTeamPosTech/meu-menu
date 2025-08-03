package com.postechfiap.meumenu.infrastructure.api.presenters.restaurante;

import com.postechfiap.meumenu.core.domain.presenters.restaurante.DeletarRestauranteOutputPort;
import com.postechfiap.meumenu.core.dtos.response.DeletarRestauranteResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class DeletarRestaurantePresenter implements DeletarRestauranteOutputPort {

    private DeletarRestauranteResponseDTO viewModel;

    @Override
    public void presentSuccess(String message) {
        this.viewModel = new DeletarRestauranteResponseDTO(
                message,
                "SUCCESS"
        );
    }

    public DeletarRestauranteResponseDTO getViewModel() {
        return viewModel;
    }
}