package com.postechfiap.meumenu.core.domain.usecases.cliente;

import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;

public interface CadastrarClienteUseCase {
    void execute(CadastrarClienteInputModel input);
}
