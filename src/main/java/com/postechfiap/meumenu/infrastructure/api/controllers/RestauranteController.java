package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.*;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AdicionarItemCardapioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarRestauranteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarRestauranteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarRestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ItemCardapioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.RestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.*;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Restaurante Controller", description = "Operações relacionadas ao Restaurante")
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final CadastrarRestauranteInputPort cadastrarRestauranteInputPort;
    private final CadastrarRestaurantePresenter cadastrarRestaurantePresenter;
    private final BuscarTodosRestaurantesInputPort buscarTodosRestaurantesInputPort;
    private final BuscarTodosRestaurantesPresenter buscarTodosRestaurantesPresenter;
    private final AdicionarItemCardapioInputPort adicionarItemCardapioInputPort;
    private final AdicionarItemCardapioPresenter adicionarItemCardapioPresenter;
    private final BuscarRestaurantePorIdInputPort buscarRestaurantePorIdInputPort;
    private final BuscarRestaurantePorIdPresenter buscarRestaurantePorIdPresenter;
    private final AtualizarRestauranteInputPort atualizarRestauranteInputPort;
    private final AtualizarRestaurantePresenter atualizarRestaurantePresenter;
    private final DeletarRestauranteInputPort deletarRestauranteInputPort;
    private final DeletarRestaurantePresenter deletarRestaurantePresenter;

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

    @Operation(
            summary = "Lista todos os restaurantes",
            description = "Este endpoint retorna uma lista de todos os restaurantes cadastrados no sistema, sem os itens do cardápio."
    )
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> buscarTodosRestaurantes() {
        buscarTodosRestaurantesInputPort.execute();
        List<RestauranteResponseDTO> responseDTOs = buscarTodosRestaurantesPresenter.getViewModel();

        if (buscarTodosRestaurantesPresenter.isNoContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(responseDTOs);
        }
    }

    @Operation(
            summary = "Adiciona um item ao cardápio de um restaurante",
            description = "Este endpoint permite que o Proprietário de um restaurante adicione um novo item ao cardápio. O proprietário logado deve ser o dono do restaurante."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @PostMapping("/{restauranteId}/itens")
    public ResponseEntity<ItemCardapioResponseDTO> adicionarItemCardapio(
            @PathVariable UUID restauranteId,
            @RequestBody @Valid AdicionarItemCardapioRequestDTO requestDTO
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioLogadoId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) {
                proprietarioLogadoId = ((com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem gerenciar itens do cardápio. Tipo de principal inesperado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }
        adicionarItemCardapioInputPort.execute(restauranteId, requestDTO.toInputModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarItemCardapioPresenter.getViewModel());
    }

    @Operation(
            summary = "Busca um restaurante por ID",
            description = "Este endpoint retorna os detalhes completos de um restaurante específico pelo seu ID, incluindo itens do cardápio."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarRestaurantePorId(@PathVariable UUID id) {
        buscarRestaurantePorIdInputPort.execute(id);
        return ResponseEntity.ok(buscarRestaurantePorIdPresenter.getViewModel());
    }

    @Operation(
            summary = "Atualiza um restaurante por ID",
            description = "Este endpoint permite que o Proprietário de um restaurante atualize seus dados. Apenas o proprietário do restaurante pode realizar esta operação."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @PutMapping("/{restauranteId}")
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(
            @PathVariable UUID restauranteId,
            @RequestBody @Valid AtualizarRestauranteRequestDTO requestDTO
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioLogadoId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof ProprietarioEntity) {
                proprietarioLogadoId = ((ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem atualizar restaurantes. Tipo de principal inesperado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }

        atualizarRestauranteInputPort.execute(restauranteId, requestDTO.toInputModel(proprietarioLogadoId), proprietarioLogadoId);
        return ResponseEntity.ok(atualizarRestaurantePresenter.getViewModel());
    }

    @Operation(
            summary = "Deleta um restaurante por ID",
            description = "Este endpoint permite que o Proprietário de um restaurante o exclua. Apenas o proprietário do restaurante pode realizar esta operação."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioLogadoId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) {
                proprietarioLogadoId = ((com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem deletar restaurantes. Tipo de principal inesperado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }
        deletarRestauranteInputPort.execute(id, proprietarioLogadoId);
        return ResponseEntity.noContent().build();
    }
}