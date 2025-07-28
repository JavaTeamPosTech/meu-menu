package com.postechfiap.meumenu.core.domain.usecases.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import java.util.List;

public interface BuscarTodosClientesUseCase {
    List<ClienteDomain> execute();
}
