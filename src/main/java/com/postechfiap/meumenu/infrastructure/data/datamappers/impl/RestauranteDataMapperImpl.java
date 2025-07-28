package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteDataMapper;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
import com.postechfiap.meumenu.infrastructure.data.datamappers.UsuarioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoRestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.HorarioFuncionamentoEntity;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import com.postechfiap.meumenu.infrastructure.model.TipoCozinhaEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestauranteDataMapperImpl implements RestauranteDataMapper {

    private final ProprietarioDataMapper proprietarioDataMapper;
    private final UsuarioDataMapper usuarioDataMapper;

    @Override
    public RestauranteEntity toEntity(RestauranteDomain domain) {
        if (domain == null) return null;

        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(domain.getId());
        entity.setCnpj(domain.getCnpj());
        entity.setRazaoSocial(domain.getRazaoSocial());
        entity.setNomeFantasia(domain.getNomeFantasia());
        entity.setInscricaoEstadual(domain.getInscricaoEstadual());
        entity.setTelefoneComercial(domain.getTelefoneComercial());

        if (domain.getProprietario() != null) {
            entity.setProprietario(proprietarioDataMapper.toEntity(domain.getProprietario()));
        }

        if (domain.getEndereco() != null) {
            EnderecoRestauranteEntity enderecoEntity = toEnderecoRestauranteEntity(domain.getEndereco());
            enderecoEntity.setRestaurante(entity);
            entity.setEndereco(enderecoEntity);
        }

        if (domain.getTiposCozinha() != null) {
            entity.setTiposCozinha(toTipoCozinhaEntityList(domain.getTiposCozinha()));
        }

        if (domain.getHorariosFuncionamento() != null) {
            List<HorarioFuncionamentoEntity> horariosEntities = toHorarioFuncionamentoEntityList(domain.getHorariosFuncionamento());
            horariosEntities.forEach(horario -> horario.setRestaurante(entity));
            entity.setHorariosFuncionamento(horariosEntities);
        }

        if (domain.getItensCardapio() != null) {
            List<ItemCardapioEntity> itensEntities = toItemCardapioEntityList(domain.getItensCardapio());
            itensEntities.forEach(item -> item.setRestaurante(entity));
            entity.setItensCardapio(itensEntities);
        }
        return entity;
    }

    @Override
    public RestauranteDomain toDomain(RestauranteEntity entity) {
        if (entity == null) return null;

        RestauranteDomain domain = new RestauranteDomain(
                entity.getId(),
                entity.getCnpj(),
                entity.getRazaoSocial(),
                entity.getNomeFantasia(),
                entity.getInscricaoEstadual(),
                entity.getTelefoneComercial(),
                entity.getProprietario() != null ? proprietarioDataMapper.toDomain(entity.getProprietario()) : null,
                entity.getEndereco() != null ? toEnderecoRestauranteDomain(entity.getEndereco()) : null,
                toTipoCozinhaDomainList(entity.getTiposCozinha()),
                toHorarioFuncionamentoDomainList(entity.getHorariosFuncionamento()),
                toItemCardapioDomainList(entity.getItensCardapio())
        );

        if (domain.getEndereco() != null) domain.getEndereco().setRestaurante(domain);
        domain.getHorariosFuncionamento().forEach(horario -> horario.setRestaurante(domain));
        domain.getItensCardapio().forEach(item -> item.setRestaurante(domain));

        return domain;
    }

    @Override
    public EnderecoRestauranteEntity toEnderecoRestauranteEntity(EnderecoRestauranteDomain domain) {
        if (domain == null) return null;
        EnderecoRestauranteEntity entity = new EnderecoRestauranteEntity();
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
    public EnderecoRestauranteDomain toEnderecoRestauranteDomain(EnderecoRestauranteEntity entity) {
        if (entity == null) return null;
        return new EnderecoRestauranteDomain(
                entity.getId(),
                entity.getEstado(),
                entity.getCidade(),
                entity.getBairro(),
                entity.getRua(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getCep(),
                null
        );
    }

    @Override
    public HorarioFuncionamentoEntity toHorarioFuncionamentoEntity(HorarioFuncionamentoDomain domain) {
        if (domain == null) return null;
        HorarioFuncionamentoEntity entity = new HorarioFuncionamentoEntity();
        entity.setId(domain.getId());
        entity.setAbre(domain.getAbre());
        entity.setFecha(domain.getFecha());
        entity.setDiaSemana(domain.getDiaSemana());
        return entity;
    }

    @Override
    public HorarioFuncionamentoDomain toHorarioFuncionamentoDomain(HorarioFuncionamentoEntity entity) {
        if (entity == null) return null;
        return new HorarioFuncionamentoDomain(
                entity.getId(),
                entity.getAbre(),
                entity.getFecha(),
                entity.getDiaSemana(),
                null
        );
    }

    @Override
    public List<HorarioFuncionamentoEntity> toHorarioFuncionamentoEntityList(List<HorarioFuncionamentoDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream().map(this::toHorarioFuncionamentoEntity).collect(Collectors.toList());
    }

    @Override
    public List<HorarioFuncionamentoDomain> toHorarioFuncionamentoDomainList(List<HorarioFuncionamentoEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(this::toHorarioFuncionamentoDomain).collect(Collectors.toList());
    }

    @Override
    public TipoCozinhaEntity toTipoCozinhaEntity(TipoCozinhaDomain domain) {
        if (domain == null) return null;
        TipoCozinhaEntity entity = new TipoCozinhaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        return entity;
    }

    @Override
    public TipoCozinhaDomain toTipoCozinhaDomain(TipoCozinhaEntity entity) {
        if (entity == null) return null;
        return new TipoCozinhaDomain(entity.getId(), entity.getNome());
    }

    @Override
    public List<TipoCozinhaEntity> toTipoCozinhaEntityList(List<TipoCozinhaDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream().map(this::toTipoCozinhaEntity).collect(Collectors.toList());
    }

    @Override
    public List<TipoCozinhaDomain> toTipoCozinhaDomainList(List<TipoCozinhaEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(this::toTipoCozinhaDomain).collect(Collectors.toList());
    }

    @Override
    public ItemCardapioEntity toItemCardapioEntity(ItemCardapioDomain domain) {
        if (domain == null) return null;
        ItemCardapioEntity entity = new ItemCardapioEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setPreco(domain.getPreco());
        entity.setDisponivelApenasNoRestaurante(domain.getDisponivelApenasNoRestaurante());
        entity.setUrlFoto(domain.getUrlFoto());
        return entity;
    }

    @Override
    public ItemCardapioDomain toItemCardapioDomain(ItemCardapioEntity entity) {
        if (entity == null) return null;
        return new ItemCardapioDomain(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.getDisponivelApenasNoRestaurante(),
                entity.getUrlFoto(),
                null
        );
    }

    @Override
    public List<ItemCardapioEntity> toItemCardapioEntityList(List<ItemCardapioDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream().map(this::toItemCardapioEntity).collect(Collectors.toList());
    }

    @Override
    public List<ItemCardapioDomain> toItemCardapioDomainList(List<ItemCardapioEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(this::toItemCardapioDomain).collect(Collectors.toList());
    }
}