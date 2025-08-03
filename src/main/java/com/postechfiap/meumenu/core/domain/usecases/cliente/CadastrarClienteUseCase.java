package com.postechfiap.meumenu.core.domain.usecases.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.dtos.cliente.CadastrarClienteInputModel;

public interface CadastrarClienteUseCase {
    ClienteDomain execute(CadastrarClienteInputModel input);
}
