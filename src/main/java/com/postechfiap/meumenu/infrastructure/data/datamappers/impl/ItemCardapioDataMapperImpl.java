package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ItemCardapioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemCardapioDataMapperImpl implements ItemCardapioDataMapper {

    @Override
    public ItemCardapioEntity toEntity(ItemCardapioDomain domain) {
        if (domain == null) {
            return null;
        }
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
    public ItemCardapioDomain toDomain(ItemCardapioEntity entity) {
        if (entity == null) {
            return null;
        }

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
}