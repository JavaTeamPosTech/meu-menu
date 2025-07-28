package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.infrastructure.model.ClienteEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ClienteDataMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteDataMapperImpl implements ClienteDataMapper {

    @Override
    public ClienteEntity toEntity(ClienteDomain domain) {
        if (domain == null) {
            return null;
        }

        ClienteEntity entity = new ClienteEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setSenha(domain.getSenha());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataAtualizacao(domain.getDataAtualizacao());
        entity.setCpf(domain.getCpf());
        entity.setDataNascimento(domain.getDataNascimento());
        entity.setGenero(domain.getGenero());
        entity.setTelefone(domain.getTelefone());
        entity.setPreferenciasAlimentares(new HashSet<>(domain.getPreferenciasAlimentares()));
        entity.setAlergias(new HashSet<>(domain.getAlergias()));
        entity.setMetodoPagamentoPreferido(domain.getMetodoPagamentoPreferido());
        entity.setUltimoPedido(domain.getUltimoPedido());
        entity.setSaldoPontos(domain.getSaldoPontos());
        entity.setClienteVip(domain.getClienteVip());
        entity.setAvaliacoesFeitas(domain.getAvaliacoesFeitas());
        entity.setNotificacoesAtivas(domain.getNotificacoesAtivas());

        List<EnderecoEntity> enderecosEntities = toEnderecoEntityList(domain.getEnderecos());
        if (enderecosEntities != null) {
            enderecosEntities.forEach(e -> e.setUsuario(entity));
        }
        entity.setEnderecos(enderecosEntities);
        return entity;
    }

    @Override
    public ClienteDomain toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }
        ClienteDomain domain = new ClienteDomain(
                entity.getId(),
                entity.getCpf(),
                entity.getDataNascimento(),
                entity.getGenero(),
                entity.getTelefone(),
                new HashSet<>(entity.getPreferenciasAlimentares()),
                new HashSet<>(entity.getAlergias()),
                entity.getMetodoPagamentoPreferido(),
                entity.getNotificacoesAtivas(),
                entity.getClienteVip(),
                entity.getSaldoPontos(),
                entity.getAvaliacoesFeitas(),
                entity.getUltimoPedido(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                toEnderecoDomainList(entity.getEnderecos())
        );
        return domain;
    }

    @Override
    public EnderecoEntity toEnderecoEntity(EnderecoDomain domain) {
        if (domain == null) return null;
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(domain.getId()); // Pode ser null para nova entidade
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

        EnderecoDomain domain = new EnderecoDomain(
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
        return domain;
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
}
