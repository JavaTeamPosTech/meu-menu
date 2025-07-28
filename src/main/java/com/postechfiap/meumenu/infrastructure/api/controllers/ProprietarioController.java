package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarProprietarioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarProprietarioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.CadastrarProprietarioPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Proprietario Controller", description = "Operações relacionadas ao Proprietário")
@RequestMapping("/proprietarios")
@RequiredArgsConstructor
public class ProprietarioController {

    private final CadastrarProprietarioInputPort cadastrarProprietarioInputPort;
    private final CadastrarProprietarioPresenter cadastrarProprietarioPresenter;

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
}