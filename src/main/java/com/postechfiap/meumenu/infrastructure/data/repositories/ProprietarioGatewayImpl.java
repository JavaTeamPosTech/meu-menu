package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProprietarioGatewayImpl implements ProprietarioGateway {

    private final ProprietarioSpringRepository proprietarioSpringRepository;
    private final ProprietarioDataMapper proprietarioDataMapper;

    @Override
    public ProprietarioDomain cadastrarProprietario(ProprietarioDomain proprietarioDomain) {
        ProprietarioEntity proprietarioEntity = proprietarioDataMapper.toEntity(proprietarioDomain);
        ProprietarioEntity savedEntity = proprietarioSpringRepository.save(proprietarioEntity);
        return proprietarioDataMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ProprietarioDomain> buscarProprietarioPorId(UUID id) {
        Optional<ProprietarioEntity> proprietarioEntityOptional = proprietarioSpringRepository.findById(id);
        return proprietarioEntityOptional.map(proprietarioDataMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return proprietarioSpringRepository.findByCpf(cpf).isPresent();
    }

    @Override
    public void deletarProprietario(UUID id) {
        proprietarioSpringRepository.deleteById(id);
    }

    @Override
    public ProprietarioDomain atualizarProprietario(ProprietarioDomain proprietarioDomain) {
        return cadastrarProprietario(proprietarioDomain);
    }
}