package com.postechfiap.meumenu.infrastructure.api.presenters.restaurante;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.CadastrarRestauranteOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
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
public class CadastrarRestaurantePresenter implements CadastrarRestauranteOutputPort {

    private CadastrarRestauranteResponseDTO viewModel;

    @Override
    public void presentSuccess(RestauranteDomain restaurante) {
        EnderecoRestauranteResponseDTO enderecoResponse = null;
        if (restaurante.getEndereco() != null) {
            enderecoResponse = mapEnderecoRestauranteDomainToResponseDTO(restaurante.getEndereco());
        }

        List<TipoCozinhaResponseDTO> tiposCozinhaResponse = null;
        if (restaurante.getTiposCozinha() != null) {
            tiposCozinhaResponse = restaurante.getTiposCozinha().stream()
                    .map(this::mapTipoCozinhaDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        List<HorarioFuncionamentoResponseDTO> horariosResponse = null;
        if (restaurante.getHorariosFuncionamento() != null) {
            horariosResponse = restaurante.getHorariosFuncionamento().stream()
                    .map(this::mapHorarioFuncionamentoDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        List<ItemCardapioResponseDTO> itensResponse = null;
        if (restaurante.getItensCardapio() != null) {
            itensResponse = restaurante.getItensCardapio().stream()
                    .map(this::mapItemCardapioDomainToResponseDTO)
                    .collect(Collectors.toList());
        }

        this.viewModel = new CadastrarRestauranteResponseDTO(
                restaurante.getId(),
                restaurante.getCnpj(),
                restaurante.getRazaoSocial(),
                restaurante.getNomeFantasia(),
                restaurante.getInscricaoEstadual(),
                restaurante.getTelefoneComercial(),
                restaurante.getProprietario().getId(),
                enderecoResponse,
                tiposCozinhaResponse,
                horariosResponse,
                itensResponse,
                "Restaurante cadastrado com sucesso!",
                "SUCCESS"
        );
    }

    @Override
    public void presentError(String message) {
        this.viewModel = new CadastrarRestauranteResponseDTO(
                null, null, null, null, null, null, null,
                null, null, null, null,
                message,
                "FAIL"
        );
    }

    private EnderecoRestauranteResponseDTO mapEnderecoRestauranteDomainToResponseDTO(EnderecoRestauranteDomain domain) {
        if (domain == null) return null;
        return new EnderecoRestauranteResponseDTO(
                domain.getId(), domain.getEstado(), domain.getCidade(), domain.getBairro(),
                domain.getRua(), domain.getNumero(), domain.getComplemento(), domain.getCep());
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

    public CadastrarRestauranteResponseDTO getViewModel() {
        return viewModel;
    }
}