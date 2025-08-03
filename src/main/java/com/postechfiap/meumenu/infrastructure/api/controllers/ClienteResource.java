package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.cliente.AtualizarClienteInputPort;
import com.postechfiap.meumenu.core.controllers.cliente.BuscarClientePorIdInputPort;
import com.postechfiap.meumenu.core.controllers.cliente.CadastrarClienteInputPort;
import com.postechfiap.meumenu.core.controllers.cliente.DeletarClienteInputPort;
import com.postechfiap.meumenu.core.dtos.request.AtualizarClienteRequestDTO;
import com.postechfiap.meumenu.core.dtos.request.CadastrarClienteRequestDTO;
import com.postechfiap.meumenu.core.dtos.response.CadastrarClienteResponseDTO;
import com.postechfiap.meumenu.core.dtos.response.ClienteResponseDTO;
import com.postechfiap.meumenu.core.dtos.response.DeletarClienteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.exceptions.ApiResponse;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.AtualizarClientePresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.BuscarClientePorIdPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.CadastrarClientePresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.cliente.DeletarClientePresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Cliente")
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteResource {

    private final AtualizarClienteInputPort atualizarClienteInputPort;
    private final AtualizarClientePresenter atualizarClientePresenter;
    private final BuscarClientePorIdInputPort buscarClientePorIdInputPort;
    private final BuscarClientePorIdPresenter buscarClientePorIdPresenter;
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
        buscarClientePorIdInputPort.execute(id);

        ClienteResponseDTO responseDTO = buscarClientePorIdPresenter.getViewModel();

        if (buscarClientePorIdPresenter.isNoContent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } else {
            return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> atualizarCliente(@PathVariable UUID id, @RequestBody @Valid AtualizarClienteRequestDTO requestDTO) {
        atualizarClienteInputPort.execute(requestDTO.toInputModel(id));
        ClienteResponseDTO viewModel = atualizarClientePresenter.getViewModel();

        if (viewModel == null) {
            String errorMessage = atualizarClientePresenter.getErrorMessage();
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        return ResponseEntity.ok(ApiResponse.success(viewModel));
    }
}