package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.gateways.AdminGateway;
import com.postechfiap.meumenu.infrastructure.data.datamappers.AdminDataMapper;
import com.postechfiap.meumenu.infrastructure.model.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminGatewayImpl implements AdminGateway {

    private final AdminSpringRepository adminSpringRepository;
    private final AdminDataMapper adminDataMapper;

    @Override
    public AdminDomain cadastrarAdmin(AdminDomain adminDomain) {
        AdminEntity adminEntity = adminDataMapper.toEntity(adminDomain);
        AdminEntity savedEntity = adminSpringRepository.save(adminEntity);
        return adminDataMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AdminDomain> buscarAdminPorId(UUID id) {
        Optional<AdminEntity> adminEntityOptional = adminSpringRepository.findById(id);
        return adminEntityOptional.map(adminDataMapper::toDomain);
    }
}