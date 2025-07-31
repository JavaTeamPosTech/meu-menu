package com.postechfiap.meumenu.infrastructure.api.presenters;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.BuscarTodosRestaurantesOutputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@NoArgsConstructor
public class BuscarTodosRestaurantesPresenter implements BuscarTodosRestaurantesOutputPort {

    private List<RestauranteResponseDTO> restaurantesViewModel;

    private boolean isNoContent = false;
    private String noContentMessage;

    @Override
    public void presentSuccess(List<RestauranteDomain> restaurantes) {
        this.restaurantesViewModel = restaurantes.stream()
                .map(this::mapRestauranteDomainToResponseDTO)
                .collect(Collectors.toList());
        this.isNoContent = false;
        this.noContentMessage = null;
    }

    @Override
    public void presentNoContent(String message) {
        this.restaurantesViewModel = Collections.emptyList();
        this.isNoContent = true;
        this.noContentMessage = message;
    }

    private RestauranteResponseDTO mapRestauranteDomainToResponseDTO(RestauranteDomain domain) {
        if (domain == null) return null;

        EnderecoRestauranteResponseDTO enderecoResponse = null;
        if (domain.getEndereco() != null) {
            enderecoResponse = mapEnderecoRestauranteDomainToResponseDTO(domain.getEndereco());
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

        List<ItemCardapioResponseDTO> itensResponse = Collections.emptyList();
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

    public List<RestauranteResponseDTO> getViewModel() {
        return restaurantesViewModel;
    }
}