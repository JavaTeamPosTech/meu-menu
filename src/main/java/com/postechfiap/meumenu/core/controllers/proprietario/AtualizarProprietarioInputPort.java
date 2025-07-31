package com.postechfiap.meumenu.core.controllers.proprietario;

import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;

public interface AtualizarProprietarioInputPort {
    void execute(AtualizarProprietarioInputModel input);
}