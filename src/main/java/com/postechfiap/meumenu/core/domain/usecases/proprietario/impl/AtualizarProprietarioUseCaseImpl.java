package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AtualizarProprietarioUseCaseImpl implements AtualizarProprietarioUseCase {

    private final ProprietarioGateway proprietarioGateway;
    private final UsuarioGateway usuarioGateway;
    private final AtualizarProprietarioOutputPort atualizarProprietarioOutputPort;

    @Override
    public ProprietarioDomain execute(AtualizarProprietarioInputModel input) {
        Optional<ProprietarioDomain> proprietarioOptional = proprietarioGateway.buscarProprietarioPorId(input.getId());

        if (proprietarioOptional.isEmpty()) {
            throw new ResourceNotFoundException("Proprietário com ID " + input.getId() + " não encontrado para atualização.");
        }
        ProprietarioDomain proprietarioExistente = proprietarioOptional.get();

        if (!input.getEmail().equals(proprietarioExistente.getEmail()) && usuarioGateway.existsByEmail(input.getEmail())) {
            throw new BusinessException("Email '" + input.getEmail() + "' já cadastrado por outro usuário.");
        }
        if (!input.getLogin().equals(proprietarioExistente.getLogin()) && usuarioGateway.existsByLogin(input.getLogin())) {
            throw new BusinessException("Login '" + input.getLogin() + "' já cadastrado por outro usuário.");
        }
        if (!input.getCpf().equals(proprietarioExistente.getCpf()) && proprietarioGateway.existsByCpf(input.getCpf())) {
            throw new BusinessException("CPF '" + input.getCpf() + "' já cadastrado por outro proprietário.");
        }
        proprietarioExistente.setNome(input.getNome());
        proprietarioExistente.setEmail(input.getEmail());
        proprietarioExistente.setLogin(input.getLogin());
        proprietarioExistente.setCpf(input.getCpf());
        proprietarioExistente.setWhatsapp(input.getWhatsapp());
        proprietarioExistente.setStatusConta(input.getStatusConta());
        proprietarioExistente.setDataAtualizacao(LocalDateTime.now());

        ProprietarioDomain proprietarioAtualizado = proprietarioGateway.atualizarProprietario(proprietarioExistente);
        atualizarProprietarioOutputPort.presentSuccess(proprietarioAtualizado);

        return proprietarioAtualizado;
    }
}