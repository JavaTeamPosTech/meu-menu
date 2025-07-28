package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteGateway {
    ClienteDomain cadastrarCliente(ClienteDomain clienteDomain);
    boolean existsByCpf(String cpf);
    Optional<ClienteDomain> buscarClientePorId(UUID id);
    List<ClienteDomain> buscarTodosClientes();
    void deletarCliente(UUID id);
    ClienteDomain atualizarCliente(ClienteDomain clienteDomain);
}
