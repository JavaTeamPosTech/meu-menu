package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarClienteOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.EnderecoResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@NoArgsConstructor
public class AtualizarClientePresenter implements AtualizarClienteOutputPort {

    private ClienteResponseDTO viewModel;

    @Override
    public void presentSuccess(ClienteDomain cliente) {
        List<EnderecoResponseDTO> enderecosResponse = null;
        if (cliente.getEnderecos() != null) {
            enderecosResponse = cliente.getEnderecos().stream()
                    .map(this::mapEnderecoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        this.viewModel = new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                cliente.getEmail(),
                cliente.getLogin(),
                cliente.getTelefone(),
                cliente.getGenero(),
                cliente.getPreferenciasAlimentares(),
                cliente.getAlergias(),
                cliente.getMetodoPagamentoPreferido(),
                cliente.getUltimoPedido(),
                cliente.getSaldoPontos(),
                cliente.getClienteVip(),
                cliente.getAvaliacoesFeitas(),
                cliente.getNotificacoesAtivas(),
                cliente.getDataCriacao(),
                cliente.getDataAtualizacao(),
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

    public ClienteResponseDTO getViewModel() {
        return viewModel;
    }
}