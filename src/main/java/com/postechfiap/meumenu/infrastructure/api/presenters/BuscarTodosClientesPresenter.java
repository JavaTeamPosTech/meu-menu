package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.EnderecoResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@NoArgsConstructor
public class BuscarTodosClientesPresenter implements BuscarTodosClientesOutputPort {

    private List<ClienteResponseDTO> clientesViewModel;
    private String noContentMessage;
    private boolean isNoContent = false;

    @Override
    public void presentSuccess(List<ClienteDomain> clientes) {
        this.clientesViewModel = clientes.stream()
                .map(this::mapClienteDomainToResponseDTO)
                .collect(Collectors.toList());
        this.isNoContent = false;
        this.noContentMessage = null;
    }

    @Override
    public void presentNoContent(String message) {
        this.clientesViewModel = Collections.emptyList();
        this.isNoContent = true;
        this.noContentMessage = message;
    }

    private ClienteResponseDTO mapClienteDomainToResponseDTO(ClienteDomain clienteDomain) {
        if (clienteDomain == null) {
            return null;
        }

        List<EnderecoResponseDTO> enderecosResponse = null;
        if (clienteDomain.getEnderecos() != null) {
            enderecosResponse = clienteDomain.getEnderecos().stream()
                    .map(this::mapEnderecoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        return new ClienteResponseDTO(
                clienteDomain.getId(),
                clienteDomain.getNome(),
                clienteDomain.getCpf(),
                clienteDomain.getDataNascimento(),
                clienteDomain.getEmail(),
                clienteDomain.getLogin(),
                clienteDomain.getTelefone(),
                clienteDomain.getGenero(),
                clienteDomain.getPreferenciasAlimentares(),
                clienteDomain.getAlergias(),
                clienteDomain.getMetodoPagamentoPreferido(),
                clienteDomain.getUltimoPedido(),
                clienteDomain.getSaldoPontos(),
                clienteDomain.getClienteVip(),
                clienteDomain.getAvaliacoesFeitas(),
                clienteDomain.getNotificacoesAtivas(),
                clienteDomain.getDataCriacao(),
                clienteDomain.getDataAtualizacao(),
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

    public List<ClienteResponseDTO> getViewModel() {
        return clientesViewModel;
    }

    public boolean isNoContent() {
        return isNoContent;
    }

    public String getNoContentMessage() {
        return noContentMessage;
    }
}