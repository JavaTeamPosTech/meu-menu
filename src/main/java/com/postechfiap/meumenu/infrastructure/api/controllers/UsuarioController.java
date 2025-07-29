package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.AlterarSenhaInputPort;
import com.postechfiap.meumenu.core.controllers.LoginInputPort;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AlterarSenhaRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.LoginRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.MensagemResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.AlterarSenhaPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.LoginPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Usuário")
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final LoginInputPort loginInputPort;
    private final LoginPresenter loginPresenter;
    private final AlterarSenhaInputPort alterarSenhaInputPort;
    private final AlterarSenhaPresenter alterarSenhaPresenter;

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

    @Operation(
            summary = "Altera a senha de um usuário autenticado",
            description = "Este endpoint permite que um usuário autenticado altere sua senha, fornecendo a senha antiga e a nova senha. O ID do usuário é pego do contexto de segurança."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/alterar-senha")
    public ResponseEntity<MensagemResponseDTO> alterarSenha(@RequestBody @Valid AlterarSenhaRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID authenticatedUserId;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.UsuarioEntity) {
                authenticatedUserId = ((com.postechfiap.meumenu.infrastructure.model.UsuarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Tipo de principal inesperado. Não foi possível obter o ID do usuário autenticado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }

        alterarSenhaInputPort.execute(requestDTO.toInputModel(authenticatedUserId));
        MensagemResponseDTO responseDTO = alterarSenhaPresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}