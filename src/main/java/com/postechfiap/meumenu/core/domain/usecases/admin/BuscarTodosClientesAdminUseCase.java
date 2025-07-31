package com.postechfiap.meumenu.core.domain.usecases.admin;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import java.util.List;

public interface BuscarTodosClientesAdminUseCase {
    List<ClienteDomain> execute();
}