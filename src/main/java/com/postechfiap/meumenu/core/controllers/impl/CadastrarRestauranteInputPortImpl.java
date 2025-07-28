package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.CadastrarRestauranteInputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.CadastrarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastrarRestauranteInputPortImpl implements CadastrarRestauranteInputPort {

    private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @Override
    public void execute(CadastrarRestauranteInputModel input) {
        cadastrarRestauranteUseCase.execute(input);
    }
}