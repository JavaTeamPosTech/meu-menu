package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain; // Importar

public interface AtualizarItemCardapioOutputPort {
    void presentSuccess(ItemCardapioDomain item);
}