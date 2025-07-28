package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeletarProprietarioInputPortImpl implements DeletarProprietarioInputPort {

    private final DeletarProprietarioUseCase deletarProprietarioUseCase;

    @Override
    public void execute(UUID id) {
        deletarProprietarioUseCase.execute(id);
    }
}