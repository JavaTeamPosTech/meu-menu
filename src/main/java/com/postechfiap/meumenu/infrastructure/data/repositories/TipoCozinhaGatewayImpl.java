package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.gateways.TipoCozinhaGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.RestauranteDataMapper;
import com.postechfiap.meumenu.infrastructure.model.TipoCozinhaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoCozinhaGatewayImpl implements TipoCozinhaGateway {

    private final TipoCozinhaSpringRepository tipoCozinhaSpringRepository;
    private final RestauranteDataMapper restauranteDataMapper;

    @Override
    public TipoCozinhaDomain buscarOuCriarTipoCozinha(String nome) {
        Optional<TipoCozinhaEntity> tipoCozinhaEntityOptional = tipoCozinhaSpringRepository.findByNome(nome);

        if (tipoCozinhaEntityOptional.isPresent()) {
            return restauranteDataMapper.toTipoCozinhaDomain(tipoCozinhaEntityOptional.get());
        } else {
            TipoCozinhaDomain novoTipoDomain = new TipoCozinhaDomain(nome);
            TipoCozinhaEntity novaTipoEntity = restauranteDataMapper.toTipoCozinhaEntity(novoTipoDomain);
            TipoCozinhaEntity savedEntity = tipoCozinhaSpringRepository.save(novaTipoEntity);
            return restauranteDataMapper.toTipoCozinhaDomain(savedEntity);
        }
    }
}