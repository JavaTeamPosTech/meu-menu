package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.BuscarTodosProprietariosOutputPort;
import com.postechfiap.meumenu.dtos.response.*;
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
public class BuscarTodosProprietariosPresenter implements BuscarTodosProprietariosOutputPort {

    private List<ProprietarioResponseDTO> viewModel;
    private boolean isNoContent = false;
    private String noContentMessage;

    @Override
    public void presentSuccess(List<ProprietarioDomain> proprietarios) {
        this.viewModel = proprietarios.stream()
                .map(this::mapProprietarioDomainToResponseDTO)
                .collect(Collectors.toList());
        this.isNoContent = false;
        this.noContentMessage = null;
    }

    @Override
    public void presentNoContent(String message) {
        this.viewModel = Collections.emptyList();
        this.isNoContent = true;
        this.noContentMessage = message;
    }

    private ProprietarioResponseDTO mapProprietarioDomainToResponseDTO(ProprietarioDomain domain) {
        if (domain == null) return null;

        List<EnderecoResponseDTO> enderecosResponse = null;
        if (domain.getEnderecos() != null) {
            enderecosResponse = domain.getEnderecos().stream()
                    .map(this::mapEnderecoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        List<RestauranteResponseDTO> restaurantesResponse = null;
        if (domain.getRestaurantes() != null) {
            restaurantesResponse = domain.getRestaurantes().stream()
                    .map(this::mapRestauranteDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        return new ProprietarioResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getCpf(),
                domain.getEmail(),
                domain.getLogin(),
                domain.getWhatsapp(),
                domain.getStatusConta(),
                domain.getDataCriacao(),
                domain.getDataAtualizacao(),
                enderecosResponse,
                restaurantesResponse
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

    private RestauranteResponseDTO mapRestauranteDomainToResponseDTO(RestauranteDomain domain) {
        if (domain == null) return null;

        EnderecoRestauranteResponseDTO enderecoResponse = null;
        if (domain.getEndereco() != null) {
            enderecoResponse = new EnderecoRestauranteResponseDTO(
                    domain.getEndereco().getId(), domain.getEndereco().getEstado(), domain.getEndereco().getCidade(), domain.getEndereco().getBairro(),
                    domain.getEndereco().getRua(), domain.getEndereco().getNumero(), domain.getEndereco().getComplemento(), domain.getEndereco().getCep());
        }

        List<TipoCozinhaResponseDTO> tiposCozinhaResponse = null;
        if (domain.getTiposCozinha() != null) {
            tiposCozinhaResponse = domain.getTiposCozinha().stream()
                    .map(this::mapTipoCozinhaDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        List<HorarioFuncionamentoResponseDTO> horariosResponse = null;
        if (domain.getHorariosFuncionamento() != null) {
            horariosResponse = domain.getHorariosFuncionamento().stream()
                    .map(this::mapHorarioFuncionamentoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        List<ItemCardapioResponseDTO> itensResponse = null; // Para lista completa, pode ser nula ou vazia
        if (domain.getItensCardapio() != null) {
            itensResponse = domain.getItensCardapio().stream()
                    .map(this::mapItemCardapioDomainToResponseDTO)
                    .collect(Collectors.toList());
        }


        return new RestauranteResponseDTO(
                domain.getId(),
                domain.getCnpj(),
                domain.getRazaoSocial(),
                domain.getNomeFantasia(),
                domain.getInscricaoEstadual(),
                domain.getTelefoneComercial(),
                enderecoResponse,
                tiposCozinhaResponse,
                horariosResponse,
                itensResponse
        );
    }

    private TipoCozinhaResponseDTO mapTipoCozinhaDomainToResponseDTO(TipoCozinhaDomain domain) {
        if (domain == null) return null;
        return new TipoCozinhaResponseDTO(domain.getId(), domain.getNome());
    }

    private HorarioFuncionamentoResponseDTO mapHorarioFuncionamentoDomainToResponseDTO(HorarioFuncionamentoDomain domain) {
        if (domain == null) return null;
        return new HorarioFuncionamentoResponseDTO(
                domain.getId(), domain.getAbre(), domain.getFecha(), domain.getDiaSemana());
    }

    private ItemCardapioResponseDTO mapItemCardapioDomainToResponseDTO(ItemCardapioDomain domain) {
        if (domain == null) return null;
        return new ItemCardapioResponseDTO(
                domain.getId(), domain.getNome(), domain.getDescricao(), domain.getPreco(),
                domain.getDisponivelApenasNoRestaurante(), domain.getUrlFoto());
    }

    public List<ProprietarioResponseDTO> getViewModel() {
        return viewModel;
    }
}