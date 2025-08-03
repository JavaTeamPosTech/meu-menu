package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.CadastrarAdminInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarAdminRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarAdminResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.BuscarTodosClientesPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.BuscarTodosProprietariosPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.admin.CadastrarAdminPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Admin")
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINENTITY')")
public class AdminResource {

    private final BuscarTodosClientesAdminInputPort  buscarTodosClientesAdminInputPort;
    private final BuscarTodosProprietariosAdminInputPort buscarTodosProprietariosAdminInputPort;
    private final BuscarTodosProprietariosPresenter buscarTodosProprietariosPresenter;
    private final BuscarTodosClientesPresenter buscarTodosClientesPresenter;
    private final CadastrarAdminInputPort cadastrarAdminInputPort;
    private final CadastrarAdminPresenter cadastrarAdminPresenter;

    @Operation(
            summary = "Realiza o cadastro de um novo usuário do tipo Admin",
            description = "Este endpoint cria um novo usuário do tipo Admin no sistema"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<CadastrarAdminResponseDTO> cadastrarAdmin(@RequestBody @Valid CadastrarAdminRequestDTO cadastrarAdminRequestDTO) {
        cadastrarAdminInputPort.execute(cadastrarAdminRequestDTO.toInputModel());
        CadastrarAdminResponseDTO responseDTO = cadastrarAdminPresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(
            summary = "Lista todos os Clientes",
            description = "Este endpoint retorna uma lista de todos os clientes cadastrados no sistema."
    )
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
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
