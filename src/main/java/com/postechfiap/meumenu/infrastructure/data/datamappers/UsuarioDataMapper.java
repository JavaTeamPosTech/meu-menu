package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;

public interface UsuarioDataMapper {
    UsuarioEntity toEntity(UsuarioDomain domain);
    UsuarioDomain toDomain(UsuarioEntity entity);
}