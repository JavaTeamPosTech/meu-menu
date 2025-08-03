package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.CadastrarProprietarioOutputPort;
import com.postechfiap.meumenu.core.dtos.response.CadastrarProprietarioResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class CadastrarProprietarioPresenter implements CadastrarProprietarioOutputPort {

    private CadastrarProprietarioResponseDTO viewModel;

    @Override
    public void presentSuccess(ProprietarioDomain proprietario) {
        this.viewModel = new CadastrarProprietarioResponseDTO(
                proprietario.getId(),
                proprietario.getNome(),
                proprietario.getEmail(),
                proprietario.getLogin(),
                proprietario.getDataCriacao(),
                "Proprietário cadastrado com sucesso!",
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new CadastrarProprietarioResponseDTO(
                null, null, null, null, null,
                message,
                "FAIL"
        );
    }

    @Override
    public boolean hasError() {
        return !"Proprietário cadastrado com sucesso!".equals(this.viewModel.message());
    }

    public CadastrarProprietarioResponseDTO getViewModel() {
        return viewModel;
    }
}