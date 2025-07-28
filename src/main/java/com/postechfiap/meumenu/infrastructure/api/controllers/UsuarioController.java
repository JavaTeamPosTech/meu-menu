package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.LoginInputPort;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.LoginRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.LoginPresenter;
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
@Tag(name = "Usuário Controller", description = "Operações relacionadas a usuários (genéricas)")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final LoginInputPort loginInputPort;
    private final LoginPresenter loginPresenter;

    @Operation(
            summary = "Realiza o login de um usuário",
            description = "Este endpoint autentica um usuário e retorna um token JWT em caso de sucesso."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        loginInputPort.execute(requestDTO.toInputModel());
        LoginResponseDTO responseDTO = loginPresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}