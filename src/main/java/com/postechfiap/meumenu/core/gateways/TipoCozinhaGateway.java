package com.postechfiap.meumenu.core.gateways;

import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain; // Importar

public interface TipoCozinhaGateway {
    TipoCozinhaDomain buscarOuCriarTipoCozinha(String nome);
}