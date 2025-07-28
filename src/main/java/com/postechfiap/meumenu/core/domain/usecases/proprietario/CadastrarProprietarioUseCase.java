package com.postechfiap.meumenu.core.domain.usecases.proprietario;

import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;

public interface CadastrarProprietarioUseCase {
    void execute(CadastrarProprietarioInputModel input);
}