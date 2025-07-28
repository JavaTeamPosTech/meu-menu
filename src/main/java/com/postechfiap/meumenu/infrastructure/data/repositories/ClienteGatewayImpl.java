package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.infrastructure.model.ClienteEntity;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ClienteDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClienteGatewayImpl implements ClienteGateway {

    private final ClienteSpringRepository clienteSpringRepository;
    private final ClienteDataMapper clienteDataMapper;

    @Override
    public ClienteDomain cadastrarCliente(ClienteDomain clienteDomain) {
        ClienteEntity clienteEntity = clienteDataMapper.toEntity(clienteDomain);
        ClienteEntity savedEntity = clienteSpringRepository.save(clienteEntity);
        return clienteDataMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return clienteSpringRepository.findByCpf(cpf).isPresent();
    }

    @Override
    public Optional<ClienteDomain> buscarClientePorId(UUID id) {
        Optional<ClienteEntity> clienteEntityOptional = clienteSpringRepository.findById(id);
        if (clienteEntityOptional.isEmpty()) {
            return Optional.empty();
        }
        return clienteEntityOptional.map(clienteDataMapper::toDomain);
    }

    @Override
    public List<ClienteDomain> buscarTodosClientes() {
        List<ClienteEntity> clientesEntities = clienteSpringRepository.findAll();
        return clientesEntities.stream()
                .map(clienteDataMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarCliente(UUID id) {
        clienteSpringRepository.deleteById(id);
    }

    @Override
    public ClienteDomain atualizarCliente(ClienteDomain clienteDomain) {
        return cadastrarCliente(clienteDomain);
    }
}