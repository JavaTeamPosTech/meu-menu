package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.infrastructure.model.ClienteEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;

import java.util.List;

public interface ClienteDataMapper {
    ClienteEntity toEntity(ClienteDomain domain);
    ClienteDomain toDomain(ClienteEntity entity);

    EnderecoEntity toEnderecoEntity(EnderecoDomain domain);
    EnderecoDomain toEnderecoDomain(EnderecoEntity entity);
    List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList);
    List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList);
}
