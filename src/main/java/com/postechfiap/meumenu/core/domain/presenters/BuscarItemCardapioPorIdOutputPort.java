package com.postechfiap.meumenu.core.domain.presenters;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;

public interface BuscarItemCardapioPorIdOutputPort {
    void presentSuccess(ItemCardapioDomain item);
}