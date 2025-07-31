package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteBasicDataMapper;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ProprietarioDataMapperImpl implements ProprietarioDataMapper {

    private final RestauranteBasicDataMapper restauranteBasicDataMapper;

    @Override
    public ProprietarioEntity toEntity(ProprietarioDomain domain) {
        if (domain == null) {
            return null;
        }
        ProprietarioEntity entity = new ProprietarioEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setSenha(domain.getSenha());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataAtualizacao(domain.getDataAtualizacao());
        entity.setCpf(domain.getCpf());
        entity.setWhatsapp(domain.getWhatsapp());
        entity.setStatusConta(domain.getStatusConta());

        List<EnderecoEntity> enderecosEntities = toEnderecoEntityList(domain.getEnderecos());
        if (enderecosEntities != null) {
            enderecosEntities.forEach(e -> e.setUsuario(entity));
        }
        entity.setEnderecos(enderecosEntities);

        List<RestauranteEntity> restaurantesEntities = toRestauranteEntityList(domain.getRestaurantes());
        if (restaurantesEntities != null) {
            restaurantesEntities.forEach(r -> r.setProprietario(entity));
        }
        entity.setRestaurantes(restaurantesEntities);

        return entity;
    }

    @Override
    public ProprietarioDomain toDomain(ProprietarioEntity entity) {
        if (entity == null) {
            return null;
        }
        ProprietarioDomain domain = new ProprietarioDomain(
                entity.getId(),
                entity.getCpf(),
                entity.getWhatsapp(),
                entity.getStatusConta(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                toEnderecoDomainList(entity.getEnderecos()),
                toRestauranteDomainList(entity.getRestaurantes())
        );
        domain.getRestaurantes().forEach(restaurante -> restaurante.setProprietario(domain));

        return domain;

    }

    @Override
    public EnderecoEntity toEnderecoEntity(EnderecoDomain domain) {
        if (domain == null) return null;
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(domain.getId());
        entity.setEstado(domain.getEstado());
        entity.setCidade(domain.getCidade());
        entity.setBairro(domain.getBairro());
        entity.setRua(domain.getRua());
        entity.setNumero(domain.getNumero());
        entity.setComplemento(domain.getComplemento());
        entity.setCep(domain.getCep());
        return entity;
    }

    @Override
    public EnderecoDomain toEnderecoDomain(EnderecoEntity entity) {
        if (entity == null) return null;

        UsuarioDomain usuarioDomain = null;
        if (entity.getUsuario() != null) {
            usuarioDomain = new UsuarioDomain(
                    entity.getUsuario().getId(),
                    entity.getUsuario().getNome(),
                    entity.getUsuario().getEmail(),
                    entity.getUsuario().getLogin(),
                    entity.getUsuario().getSenha(),
                    entity.getUsuario().getDataCriacao(),
                    entity.getUsuario().getDataAtualizacao(),
                    null
            );
        }
        return new EnderecoDomain(
                entity.getId(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getBairro(),
                entity.getRua(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getCep(),
                usuarioDomain
        );
    }

    @Override
    public List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream()
                .map(this::toEnderecoEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(this::toEnderecoDomain)
                .collect(Collectors.toList());
    }

    private RestauranteEntity toRestauranteEntity(RestauranteDomain domain) {
        if (domain == null) return null;
        return restauranteBasicDataMapper.toBasicEntity(domain);
    }

    private RestauranteDomain toRestauranteDomain(RestauranteEntity entity) {
        if (entity == null) return null;
        return restauranteBasicDataMapper.toBasicDomain(entity);
    }

    private List<RestauranteEntity> toRestauranteEntityList(List<RestauranteDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream()
                .map(this::toRestauranteEntity)
                .collect(Collectors.toList());
    }

    private List<RestauranteDomain> toRestauranteDomainList(List<RestauranteEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(this::toRestauranteDomain)
                .collect(Collectors.toList());
    }
}