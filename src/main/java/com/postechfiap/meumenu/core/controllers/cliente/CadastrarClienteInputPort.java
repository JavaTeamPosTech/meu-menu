package com.postechfiap.meumenu.core.controllers.cliente;

import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;

public interface CadastrarClienteInputPort {
    void execute(CadastrarClienteInputModel input);
}
