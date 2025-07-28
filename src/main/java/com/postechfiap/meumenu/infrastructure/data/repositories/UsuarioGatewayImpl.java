package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.UsuarioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioSpringRepository usuarioSpringRepository;
    private final UsuarioDataMapper usuarioDataMapper;

    @Override
    public boolean existsByLogin(String login) {
        return usuarioSpringRepository.findByLogin(login).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioSpringRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<UsuarioDomain> buscarUsuarioPorId(UUID id) {
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioSpringRepository.findById(id);
        return usuarioEntityOptional.map(usuarioDataMapper::toDomain);
    }

    @Override
    public UsuarioDomain atualizarUsuario(UsuarioDomain usuarioDomain) {
        if (usuarioDomain.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo para atualização.");
        }
        Optional<UsuarioEntity> existingEntityOptional = usuarioSpringRepository.findById(usuarioDomain.getId());

        if (existingEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado para atualização.");
        }

        UsuarioEntity existingEntity = existingEntityOptional.get();
        existingEntity.setNome(usuarioDomain.getNome());
        existingEntity.setEmail(usuarioDomain.getEmail());
        existingEntity.setLogin(usuarioDomain.getLogin());
        existingEntity.setSenha(usuarioDomain.getSenha()); // Senha já criptografada
        existingEntity.setDataAtualizacao(usuarioDomain.getDataAtualizacao());
        UsuarioEntity updatedEntity = usuarioSpringRepository.save(existingEntity);

        return usuarioDataMapper.toDomain(updatedEntity);
    }
}