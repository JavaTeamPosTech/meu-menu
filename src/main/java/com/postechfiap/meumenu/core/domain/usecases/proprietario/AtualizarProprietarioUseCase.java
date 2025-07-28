package com.postechfiap.meumenu.core.domain.usecases.proprietario;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.dtos.proprietario.AtualizarProprietarioInputModel;

public interface AtualizarProprietarioUseCase {
    ProprietarioDomain execute(AtualizarProprietarioInputModel input);
}