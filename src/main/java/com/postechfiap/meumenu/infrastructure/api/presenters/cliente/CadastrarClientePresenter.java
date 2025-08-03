package com.postechfiap.meumenu.infrastructure.api.presenters.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.CadastrarClienteOutputPort;
import com.postechfiap.meumenu.core.dtos.response.CadastrarClienteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CadastrarClientePresenter implements CadastrarClienteOutputPort {

    private CadastrarClienteResponseDTO viewModel;

    public void presentSuccess(ClienteDomain cliente) {
        this.viewModel = new CadastrarClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getLogin(),
                cliente.getDataCriacao(),
                " Cliente cadastrado com sucesso!",
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new CadastrarClienteResponseDTO(
        null,
        null,
        null,
        null,
        null,
        message,
        "FAIL"
        );
    }

    public CadastrarClienteResponseDTO getViewModel() {
        return this.viewModel;
    }
}