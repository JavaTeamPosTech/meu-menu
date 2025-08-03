package com.postechfiap.meumenu.core.domain.usecases.cliente.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.usecases.cliente.AtualizarClienteUseCase;
import com.postechfiap.meumenu.core.dtos.cliente.AtualizarClienteInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class AtualizarClienteUseCaseImpl implements AtualizarClienteUseCase {

    private final ClienteGateway clienteGateway;
    private final UsuarioGateway usuarioGateway;


    @Override
    public ClienteDomain execute(AtualizarClienteInputModel input) {
        Optional<ClienteDomain> clienteOptional = clienteGateway.buscarClientePorId(input.getId());

        if (clienteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Cliente com ID " + input.getId() + " não encontrado para atualização.");
        }
        ClienteDomain clienteExistente = clienteOptional.get();

        if (!input.getEmail().equals(clienteExistente.getEmail()) && usuarioGateway.existsByEmail(input.getEmail())) {
            throw new BusinessException("Email '" + input.getEmail() + "' já cadastrado por outro usuário.");
        }
        if (!input.getLogin().equals(clienteExistente.getLogin()) && usuarioGateway.existsByLogin(input.getLogin())) {
            throw new BusinessException("Login '" + input.getLogin() + "' já cadastrado por outro usuário.");
        }
        if (!input.getCpf().equals(clienteExistente.getCpf()) && clienteGateway.existsByCpf(input.getCpf())) {
            throw new BusinessException("CPF '" + input.getCpf() + "' já cadastrado por outro cliente.");
        }

        clienteExistente.setNome(input.getNome());
        clienteExistente.setEmail(input.getEmail());
        clienteExistente.setLogin(input.getLogin());
        clienteExistente.setCpf(input.getCpf());
        clienteExistente.setDataNascimento(input.getDataNascimento());
        clienteExistente.setGenero(input.getGenero());
        clienteExistente.setTelefone(input.getTelefone());
        clienteExistente.setPreferenciasAlimentares(input.getPreferenciasAlimentares());
        clienteExistente.setAlergias(input.getAlergias());
        clienteExistente.setMetodoPagamentoPreferido(input.getMetodoPagamentoPreferido());
        clienteExistente.setNotificacoesAtivas(input.getNotificacoesAtivas());
        clienteExistente.setDataAtualizacao(LocalDateTime.now());

        // TODO Endereços e senha não são atualizados aqui
        // O clienteVip, saldoPontos, avaliacoesFeitas e ultimoPedido também são gerenciados internamente, não via update geral.

        return clienteGateway.atualizarCliente(clienteExistente);

    }
}