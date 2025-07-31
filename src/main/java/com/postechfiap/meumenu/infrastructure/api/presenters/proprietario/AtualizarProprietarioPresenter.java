package com.postechfiap.meumenu.infrastructure.api.presenters.proprietario;

import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.AtualizarProprietarioOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
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

        List<RestauranteResponseDTO> restaurantesResponse = null;
        if (proprietario.getRestaurantes() != null) {
            restaurantesResponse = proprietario.getRestaurantes().stream()
                    .map(this::mapRestauranteDomainToResponseDTO)
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

    public ProprietarioResponseDTO getViewModel() {
        return viewModel;
    }
}