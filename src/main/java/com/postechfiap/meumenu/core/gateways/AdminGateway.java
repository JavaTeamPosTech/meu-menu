package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import java.util.Optional;
import java.util.UUID;

public interface AdminGateway {
    AdminDomain cadastrarAdmin(AdminDomain adminDomain);
    Optional<AdminDomain> buscarAdminPorId(UUID id);
}