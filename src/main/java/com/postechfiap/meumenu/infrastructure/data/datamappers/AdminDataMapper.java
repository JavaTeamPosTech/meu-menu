package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.infrastructure.model.AdminEntity;

public interface AdminDataMapper {
    AdminEntity toEntity(AdminDomain domain);
    AdminDomain toDomain(AdminEntity entity);
}