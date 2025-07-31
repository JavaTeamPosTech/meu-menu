package com.postechfiap.meumenu.infrastructure.api.presenters.usuario;

import com.postechfiap.meumenu.core.domain.presenters.usuario.LoginOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LoginPresenter implements LoginOutputPort {

    private LoginResponseDTO viewModel;

    @Override
    public void presentSuccess(String token) {
        this.viewModel = new LoginResponseDTO(
                token,
                "Login realizado com sucesso!",
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new LoginResponseDTO(
                null,
                message,
                "FAIL"
        );
    }

    public LoginResponseDTO getViewModel() {
        return viewModel;
    }
}