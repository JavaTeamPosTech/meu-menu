package com.postechfiap.meumenu.core.controllers;

import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel; // Importar

public interface CadastrarProprietarioInputPort {
    void execute(CadastrarProprietarioInputModel input);
}