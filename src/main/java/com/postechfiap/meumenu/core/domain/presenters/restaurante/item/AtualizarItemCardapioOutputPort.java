package com.postechfiap.meumenu.core.domain.presenters.restaurante.item;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain; // Importar

public interface AtualizarItemCardapioOutputPort {
    void presentSuccess(ItemCardapioDomain item);
}