package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarProprietarioOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.EnderecoResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@NoArgsConstructor
public class AtualizarProprietarioPresenter implements AtualizarProprietarioOutputPort {

    private ProprietarioResponseDTO viewModel;

    @Override
    public void presentSuccess(ProprietarioDomain proprietario) {
        List<EnderecoResponseDTO> enderecosResponse = null;
        if (proprietario.getEnderecos() != null) {
            enderecosResponse = proprietario.getEnderecos().stream()
                    .map(this::mapEnderecoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        this.viewModel = new ProprietarioResponseDTO(
                proprietario.getId(),
                proprietario.getNome(),
                proprietario.getCpf(),
                proprietario.getEmail(),
                proprietario.getLogin(),
                proprietario.getWhatsapp(),
                proprietario.getStatusConta(),
                proprietario.getDataCriacao(),
                proprietario.getDataAtualizacao(),
                enderecosResponse
        );
    }

    private EnderecoResponseDTO mapEnderecoDomainToResponseDTO(EnderecoDomain enderecoDomain) {
        if (enderecoDomain == null) {
            return null;
        }
        return new EnderecoResponseDTO(
                enderecoDomain.getId(),
                enderecoDomain.getEstado(),
                enderecoDomain.getCidade(),
                enderecoDomain.getBairro(),
                enderecoDomain.getRua(),
                enderecoDomain.getNumero(),
                enderecoDomain.getComplemento(),
                enderecoDomain.getCep()
        );
    }

    public ProprietarioResponseDTO getViewModel() {
        return viewModel;
    }
}