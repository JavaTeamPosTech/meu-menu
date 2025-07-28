package com.postechfiap.meumenu.core.domain.usecases.usuario.impl;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain; // Importar UsuarioDomain
import com.postechfiap.meumenu.core.domain.presenters.AlterarSenhaOutputPort; // NOVO: Interface do Presenter
import com.postechfiap.meumenu.core.domain.services.PasswordService; // Para criptografia
import com.postechfiap.meumenu.core.domain.usecases.usuario.AlterarSenhaUseCase;
import com.postechfiap.meumenu.core.dtos.usuario.AlterarSenhaInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway; // Para buscar e atualizar usuário
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Para dataAtualizacao
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarSenhaUseCaseImpl implements AlterarSenhaUseCase {

    private final UsuarioGateway usuarioGateway;
    private final PasswordService passwordService;
    private final AlterarSenhaOutputPort alterarSenhaOutputPort;

    @Override
    public void execute(AlterarSenhaInputModel input) {
        Optional<UsuarioDomain> usuarioOptional = usuarioGateway.buscarUsuarioPorId(input.getUsuarioId());

        if (usuarioOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuário com ID " + input.getUsuarioId() + " não encontrado para alteração de senha.");
        }
        UsuarioDomain usuarioExistente = usuarioOptional.get();

        if (!passwordService.matches(input.getSenhaAntiga(), usuarioExistente.getSenha())) {
            alterarSenhaOutputPort.presentError("Senha antiga incorreta.");
            throw new BusinessException("Senha antiga incorreta.");
        }

        String novaSenhaCriptografada = passwordService.encryptPassword(input.getNovaSenha());

        usuarioExistente.setSenha(novaSenhaCriptografada);
        usuarioExistente.setDataAtualizacao(LocalDateTime.now());

        usuarioGateway.atualizarUsuario(usuarioExistente);
        alterarSenhaOutputPort.presentSuccess("Senha alterada com sucesso para o usuário ID " + input.getUsuarioId() + ".");
    }
}