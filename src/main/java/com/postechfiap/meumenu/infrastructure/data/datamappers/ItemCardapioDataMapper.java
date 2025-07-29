package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;

public interface ItemCardapioDataMapper {
    ItemCardapioEntity toEntity(ItemCardapioDomain domain);
    ItemCardapioDomain toDomain(ItemCardapioEntity entity);
}