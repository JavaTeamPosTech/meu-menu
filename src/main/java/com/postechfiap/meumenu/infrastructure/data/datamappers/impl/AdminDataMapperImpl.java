package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.infrastructure.data.datamappers.AdminDataMapper;
import com.postechfiap.meumenu.infrastructure.model.AdminEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import com.postechfiap.meumenu.infrastructure.data.datamappers.UsuarioDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminDataMapperImpl implements AdminDataMapper {

    private final UsuarioDataMapper usuarioDataMapper;

    @Override
    public AdminEntity toEntity(AdminDomain domain) {
        if (domain == null) return null;
        AdminEntity entity = new AdminEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setSenha(domain.getSenha());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataAtualizacao(domain.getDataAtualizacao());

        List<EnderecoEntity> enderecosEntities = toEnderecoEntityList(domain.getEnderecos());
        if (enderecosEntities != null) {
            enderecosEntities.forEach(e -> e.setUsuario(entity));
        }
        entity.setEnderecos(enderecosEntities);

        return entity;
    }

    @Override
    public AdminDomain toDomain(AdminEntity entity) {
        if (entity == null) return null;
        AdminDomain domain = new AdminDomain(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                toEnderecoDomainList(entity.getEnderecos())
        );
        if (domain.getEnderecos() != null) {
            domain.getEnderecos().forEach(e -> e.setUsuario(domain));
        }
        return domain;
    }

    private List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList) {
        if (domainList == null) return null;
        return domainList.stream()
                .map(this::mapEnderecoDomainToEntity)
                .collect(Collectors.toList());
    }

    private List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(this::mapEnderecoEntityToDomain)
                .collect(Collectors.toList());
    }

    private EnderecoEntity mapEnderecoDomainToEntity(EnderecoDomain domain) {
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

    private EnderecoDomain mapEnderecoEntityToDomain(EnderecoEntity entity) {
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

        return  new EnderecoDomain(
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
}