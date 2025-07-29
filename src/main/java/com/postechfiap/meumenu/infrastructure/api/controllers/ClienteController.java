package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.*;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarClienteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarClienteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.DeletarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.*;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Cliente")
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final AtualizarClienteInputPort atualizarClienteInputPort;
    private final AtualizarClientePresenter atualizarClientePresenter;
    private final BuscarClientePorIdInputPort buscarClientePorIdInputPort;
    private final BuscarClientePorIdPresenter buscarClientePorIdPresenter;
    private final BuscarTodosClientesInputPort buscarTodosClientesInputPort;
    private final BuscarTodosClientesPresenter buscarTodosClientesPresenter;
    private final CadastrarClienteInputPort cadastrarClienteInputPort;
    private final CadastrarClientePresenter cadastrarClientePresenter;
    private final DeletarClienteInputPort deletarClienteInputPort;
    private final DeletarClientePresenter deletarClientePresenter;

    @Operation(
            summary = "Realiza o cadastro de um novo usuário do tipo Cliente",
            description = "Este endpoint cria um novo usuário do tipo Cliente no sistema"
    )
    @PostMapping
    public ResponseEntity<CadastrarClienteResponseDTO> cadastrarCliente(@RequestBody @Valid CadastrarClienteRequestDTO cadastrarClienteRequestDTO) {
        cadastrarClienteInputPort.execute(cadastrarClienteRequestDTO.toInputModel());
        CadastrarClienteResponseDTO responseDTO = cadastrarClientePresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(
            summary = "Busca um cliente por ID",
            description = "Este endpoint retorna os detalhes de um cliente específico pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable UUID id) {
        Optional<ClienteDomain> cliente = buscarClientePorIdInputPort.execute(id);
        ClienteResponseDTO responseDTO = buscarClientePorIdPresenter.getViewModel();

        if (cliente.isPresent()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @Operation(
            summary = "Lista todos os clientes",
            description = "Este endpoint retorna uma lista de todos os clientes cadastrados no sistema."
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> buscarTodosClientes() {
        buscarTodosClientesInputPort.execute();

        List<ClienteResponseDTO> responseDTOs = buscarTodosClientesPresenter.getViewModel();

        if (buscarTodosClientesPresenter.isNoContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responseDTOs);
        }
    }
    @Operation(
            summary = "Deleta um cliente por ID",
            description = "Este endpoint deleta um cliente específico pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletarClienteResponseDTO> deletarCliente(@PathVariable UUID id) {
        deletarClienteInputPort.execute(id);

//         return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(deletarClientePresenter.getViewModel());
    }

    @Operation(
            summary = "Atualiza um cliente por ID",
            description = "Este endpoint atualiza os dados de um cliente existente pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable UUID id, @RequestBody @Valid AtualizarClienteRequestDTO requestDTO) {
        atualizarClienteInputPort.execute(requestDTO.toInputModel(id));
        return ResponseEntity.ok(atualizarClientePresenter.getViewModel());
    }
}