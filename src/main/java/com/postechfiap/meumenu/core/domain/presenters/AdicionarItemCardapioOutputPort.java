package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain; // Importar

public interface AdicionarItemCardapioOutputPort {
    void presentSuccess(ItemCardapioDomain item);
}