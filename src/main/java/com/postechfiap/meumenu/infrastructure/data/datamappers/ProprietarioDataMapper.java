package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;

import java.util.List;

public interface ProprietarioDataMapper {
    ProprietarioEntity toEntity(ProprietarioDomain domain);
    ProprietarioDomain toDomain(ProprietarioEntity entity);
    EnderecoEntity toEnderecoEntity(EnderecoDomain domain);
    EnderecoDomain toEnderecoDomain(EnderecoEntity entity);
    List<EnderecoEntity> toEnderecoEntityList(List<EnderecoDomain> domainList);
    List<EnderecoDomain> toEnderecoDomainList(List<EnderecoEntity> entityList);
}