package com.postechfiap.meumenu.infrastructure.api.presenters.admin;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.domain.presenters.admin.CadastrarAdminOutputPort;
import com.postechfiap.meumenu.dtos.response.CadastrarAdminResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class CadastrarAdminPresenter implements CadastrarAdminOutputPort {

    private CadastrarAdminResponseDTO viewModel;

    @Override
    public void presentSuccess(AdminDomain admin) {
        this.viewModel = new CadastrarAdminResponseDTO(
                admin.getId(),
                admin.getNome(),
                admin.getEmail(),
                admin.getLogin(),
                admin.getDataCriacao(),
                "Admin cadastrado com sucesso!",
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new CadastrarAdminResponseDTO(
                null,
                null,
                null,
                null,
                null,
                message,
                "FAIL"
        );
    }

    public CadastrarAdminResponseDTO getViewModel() {
        return viewModel;
    }
}