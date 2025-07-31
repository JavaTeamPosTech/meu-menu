package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProprietarioGateway {
    ProprietarioDomain cadastrarProprietario(ProprietarioDomain proprietarioDomain);
    Optional<ProprietarioDomain> buscarProprietarioPorId(UUID id);
    boolean existsByCpf(String cpf);
    void deletarProprietario(UUID id);
    ProprietarioDomain atualizarProprietario(ProprietarioDomain proprietarioDomain);
    List<ProprietarioDomain> buscarTodosProprietarios();
}