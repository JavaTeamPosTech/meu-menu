package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.controllers.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.BuscarTodosClientesPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.BuscarTodosProprietariosPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Admin")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BuscarTodosClientesAdminInputPort  buscarTodosClientesAdminInputPort;
    private final BuscarTodosProprietariosAdminInputPort buscarTodosProprietariosAdminInputPort;
    private final BuscarTodosProprietariosPresenter buscarTodosProprietariosPresenter;
    private final BuscarTodosClientesPresenter buscarTodosClientesPresenter;

    @Operation(
            summary = "Lista todos os Clientes",
            description = "Este endpoint retorna uma lista de todos os clientes cadastrados no sistema."
    )
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteResponseDTO>> buscarTodosClientes() {
        buscarTodosClientesAdminInputPort.execute();

        List<ClienteResponseDTO> responseDTOs = buscarTodosClientesPresenter.getViewModel();

        if (buscarTodosClientesPresenter.isNoContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responseDTOs);
        }
    }

    @Operation(
            summary = "Lista todos os Proprietários",
            description = "Este endpoint retorna uma lista de todos os proprietarios cadastrados no sistema."
    )
    @GetMapping("/proprietarios")
    public ResponseEntity<List<ProprietarioResponseDTO>> buscarTodosProprietarios() {
        buscarTodosProprietariosAdminInputPort.execute();

        List<ProprietarioResponseDTO> responseDTOs = buscarTodosProprietariosPresenter.getViewModel();

        if (buscarTodosProprietariosPresenter.isNoContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responseDTOs);
        }
    }

}
