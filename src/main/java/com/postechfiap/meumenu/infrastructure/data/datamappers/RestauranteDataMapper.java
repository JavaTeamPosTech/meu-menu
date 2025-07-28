package com.postechfiap.meumenu.infrastructure.data.datamappers;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoRestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.HorarioFuncionamentoEntity;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import com.postechfiap.meumenu.infrastructure.model.TipoCozinhaEntity;

import java.util.List;

public interface RestauranteDataMapper {
    RestauranteEntity toEntity(RestauranteDomain domain);
    RestauranteDomain toDomain(RestauranteEntity entity);

    EnderecoRestauranteEntity toEnderecoRestauranteEntity(EnderecoRestauranteDomain domain);
    EnderecoRestauranteDomain toEnderecoRestauranteDomain(EnderecoRestauranteEntity entity);

    HorarioFuncionamentoEntity toHorarioFuncionamentoEntity(HorarioFuncionamentoDomain domain);
    HorarioFuncionamentoDomain toHorarioFuncionamentoDomain(HorarioFuncionamentoEntity entity);
    List<HorarioFuncionamentoEntity> toHorarioFuncionamentoEntityList(List<HorarioFuncionamentoDomain> domainList);
    List<HorarioFuncionamentoDomain> toHorarioFuncionamentoDomainList(List<HorarioFuncionamentoEntity> entityList);

    TipoCozinhaEntity toTipoCozinhaEntity(TipoCozinhaDomain domain);
    TipoCozinhaDomain toTipoCozinhaDomain(TipoCozinhaEntity entity);
    List<TipoCozinhaEntity> toTipoCozinhaEntityList(List<TipoCozinhaDomain> domainList);
    List<TipoCozinhaDomain> toTipoCozinhaDomainList(List<TipoCozinhaEntity> entityList);

    ItemCardapioEntity toItemCardapioEntity(ItemCardapioDomain domain);
    ItemCardapioDomain toItemCardapioDomain(ItemCardapioEntity entity);
    List<ItemCardapioEntity> toItemCardapioEntityList(List<ItemCardapioDomain> domainList);
    List<ItemCardapioDomain> toItemCardapioDomainList(List<ItemCardapioEntity> entityList);
}