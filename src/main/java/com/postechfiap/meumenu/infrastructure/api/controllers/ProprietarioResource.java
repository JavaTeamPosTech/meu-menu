package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.proprietario.AtualizarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.core.dtos.request.AtualizarProprietarioRequestDTO;
import com.postechfiap.meumenu.core.dtos.request.CadastrarProprietarioRequestDTO;
import com.postechfiap.meumenu.core.dtos.response.CadastrarProprietarioResponseDTO;
import com.postechfiap.meumenu.core.dtos.response.DeletarProprietarioResponseDTO;
import com.postechfiap.meumenu.core.dtos.response.ProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.exceptions.ApiResponse;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.AtualizarProprietarioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.BuscarProprietarioPorIdPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.CadastrarProprietarioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.DeletarProprietarioPresenter;
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
@Tag(name = "Proprietario")
@RequestMapping("/proprietarios")
@RequiredArgsConstructor
public class ProprietarioResource {

    private final CadastrarProprietarioInputPort cadastrarProprietarioInputPort;
    private final CadastrarProprietarioPresenter cadastrarProprietarioPresenter;
    private final BuscarProprietarioPorIdInputPort buscarProprietarioPorIdInputPort;
    private final BuscarProprietarioPorIdPresenter buscarProprietarioPorIdPresenter;
    private final DeletarProprietarioInputPort deletarProprietarioInputPort;
    private final DeletarProprietarioPresenter deletarProprietarioPresenter;
    private final AtualizarProprietarioInputPort atualizarProprietarioInputPort;
    private final AtualizarProprietarioPresenter atualizarProprietarioPresenter;

    @Operation(
            summary = "Realiza o cadastro de um novo usuário do tipo Proprietário",
            description = "Este endpoint cria um novo usuário do tipo Proprietário no sistema."
    )
    @PostMapping
    public ResponseEntity<CadastrarProprietarioResponseDTO> cadastrarProprietario(@RequestBody @Valid CadastrarProprietarioRequestDTO requestDTO) {
        cadastrarProprietarioInputPort.execute(requestDTO.toInputModel());

        CadastrarProprietarioResponseDTO responseDTO = cadastrarProprietarioPresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(
            summary = "Busca um proprietário por ID",
            description = "Este endpoint retorna os detalhes de um proprietário específico pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<ProprietarioResponseDTO> buscarProprietarioPorId(@PathVariable UUID id) {

        buscarProprietarioPorIdInputPort.execute(id);

        ProprietarioResponseDTO responseDTO = buscarProprietarioPorIdPresenter.getViewModel();

        if (buscarProprietarioPorIdPresenter.isNoContent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } else {
            return ResponseEntity.ok(responseDTO);
        }


    }

    @Operation(
            summary = "Deleta um proprietário por ID",
            description = "Este endpoint deleta um proprietário específico pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeletarProprietarioResponseDTO> deletarProprietario(@PathVariable UUID id) {
        deletarProprietarioInputPort.execute(id);

//        return ResponseEntity.noContent().build();
        DeletarProprietarioResponseDTO responseDTO = deletarProprietarioPresenter.getViewModel();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Atualiza um proprietário por ID",
            description = "Este endpoint atualiza os dados de um proprietário existente pelo seu ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProprietarioResponseDTO>> atualizarProprietario(@PathVariable UUID id, @RequestBody @Valid AtualizarProprietarioRequestDTO requestDTO) {
        atualizarProprietarioInputPort.execute(requestDTO.toInputModel(id));

        ProprietarioResponseDTO viewModel = atualizarProprietarioPresenter.getViewModel();

        if (viewModel == null) {
            String errorMessage = atualizarProprietarioPresenter.getErrorMessage();
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        return ResponseEntity.ok(ApiResponse.success(viewModel));
    }
}