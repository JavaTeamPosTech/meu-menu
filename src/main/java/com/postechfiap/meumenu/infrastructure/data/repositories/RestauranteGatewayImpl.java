package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ItemCardapioDataMapper;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteDataMapper;
import com.postechfiap.meumenu.infrastructure.model.EnderecoRestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.HorarioFuncionamentoEntity;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class RestauranteGatewayImpl implements RestauranteGateway {

    private final RestauranteSpringRepository restauranteSpringRepository;
    private final ItemCardapioSpringRepository itemCardapioSpringRepository;
    private final RestauranteDataMapper restauranteDataMapper;
    private final ItemCardapioDataMapper itemCardapioDataMapper;


    @Override
    public RestauranteDomain cadastrarRestaurante(RestauranteDomain restauranteDomain) {
        RestauranteEntity restauranteEntity = restauranteDataMapper.toEntity(restauranteDomain);
        RestauranteEntity savedEntity = restauranteSpringRepository.save(restauranteEntity);
        return restauranteDataMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return restauranteSpringRepository.findByCnpj(cnpj).isPresent();
    }

    @Override
    public List<RestauranteDomain> buscarTodosRestaurantes() {
        List<RestauranteEntity> restaurantesEntities = restauranteSpringRepository.findAll();
        return restaurantesEntities.stream()
                .map(restauranteDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RestauranteDomain> buscarRestaurantePorId(UUID id) {
        Optional<RestauranteEntity> restauranteEntityOptional = restauranteSpringRepository.findById(id);
        return restauranteEntityOptional.map(restauranteDataMapper::toDomain);
    }

    @Override
    public RestauranteDomain atualizarRestaurante(RestauranteDomain restauranteDomain) {
        RestauranteEntity existingEntity = restauranteSpringRepository.findById(restauranteDomain.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado para atualização."));

        existingEntity.setCnpj(restauranteDomain.getCnpj());
        existingEntity.setRazaoSocial(restauranteDomain.getRazaoSocial());
        existingEntity.setNomeFantasia(restauranteDomain.getNomeFantasia());
        existingEntity.setInscricaoEstadual(restauranteDomain.getInscricaoEstadual());
        existingEntity.setTelefoneComercial(restauranteDomain.getTelefoneComercial());

        if (restauranteDomain.getEndereco() == null) {
            if (existingEntity.getEndereco() != null) {
                existingEntity.setEndereco(null);
            }
        } else {
            if (existingEntity.getEndereco() != null) {
                EnderecoRestauranteEntity enderecoEntity = existingEntity.getEndereco();
                enderecoEntity.setEstado(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getEstado());
                enderecoEntity.setCidade(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getCidade());
                enderecoEntity.setBairro(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getBairro());
                enderecoEntity.setRua(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getRua());
                enderecoEntity.setNumero(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getNumero());
                enderecoEntity.setComplemento(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getComplemento());
                enderecoEntity.setCep(restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco()).getCep());
            } else {
                EnderecoRestauranteEntity novoEnderecoEntity = restauranteDataMapper.toEnderecoRestauranteEntity(restauranteDomain.getEndereco());
                novoEnderecoEntity.setRestaurante(existingEntity);
                existingEntity.setEndereco(novoEnderecoEntity);
            }
        }

        existingEntity.getTiposCozinha().clear();
        if (restauranteDomain.getTiposCozinha() != null) {
            existingEntity.getTiposCozinha().addAll(restauranteDataMapper.toTipoCozinhaEntityList(restauranteDomain.getTiposCozinha()));
        }

        existingEntity.getHorariosFuncionamento().clear();
        if (restauranteDomain.getHorariosFuncionamento() != null) {
            List<HorarioFuncionamentoEntity> novosHorariosEntities = restauranteDataMapper.toHorarioFuncionamentoEntityList(restauranteDomain.getHorariosFuncionamento());
            novosHorariosEntities.forEach(horario -> horario.setRestaurante(existingEntity));
            existingEntity.getHorariosFuncionamento().addAll(novosHorariosEntities);
        }

        RestauranteEntity updatedEntity = restauranteSpringRepository.save(existingEntity);
        return restauranteDataMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public ItemCardapioDomain adicionarItemCardapio(UUID restauranteId, ItemCardapioDomain itemCardapioDomain) {
        RestauranteEntity restauranteEntity = restauranteSpringRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado para adicionar item."));

        ItemCardapioEntity itemCardapioEntity = itemCardapioDataMapper.toEntity(itemCardapioDomain);
        itemCardapioEntity.setRestaurante(restauranteEntity);
        ItemCardapioEntity savedItemEntity = itemCardapioSpringRepository.save(itemCardapioEntity);

        return itemCardapioDataMapper.toDomain(savedItemEntity);
    }

    @Override
    public void deletarRestaurante(UUID id) {
        restauranteSpringRepository.deleteById(id);
    }
}