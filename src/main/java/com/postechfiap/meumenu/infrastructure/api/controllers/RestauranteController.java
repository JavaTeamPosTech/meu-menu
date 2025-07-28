package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.CadastrarRestauranteInputPort; // Importar
import com.postechfiap.meumenu.core.exceptions.BusinessException; // Para o erro de tipo de principal
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarRestauranteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarRestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.CadastrarRestaurantePresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Para @PreAuthorize
import org.springframework.security.core.Authentication; // Para obter o principal
import org.springframework.security.core.context.SecurityContextHolder; // Para obter o contexto
import org.springframework.security.core.userdetails.UserDetails; // Para o cast do principal
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Tag(name = "Restaurante Controller", description = "Operações relacionadas ao Restaurante")
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final CadastrarRestauranteInputPort cadastrarRestauranteInputPort;
    private final CadastrarRestaurantePresenter cadastrarRestaurantePresenter;

    @Operation(
            summary = "Realiza o cadastro de um novo restaurante",
            description = "Este endpoint permite que um Proprietário logado cadastre um novo restaurante no sistema."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @PostMapping
    public ResponseEntity<CadastrarRestauranteResponseDTO> cadastrarRestaurante(@RequestBody @Valid CadastrarRestauranteRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) {
                proprietarioId = ((com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem cadastrar restaurantes. Tipo de principal inesperado ou role inválida.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }

        cadastrarRestauranteInputPort.execute(requestDTO.toInputModel(proprietarioId));
        CadastrarRestauranteResponseDTO responseDTO = cadastrarRestaurantePresenter.getViewModel();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}