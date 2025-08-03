package com.postechfiap.meumenu.infrastructure.api.controllers;

import com.postechfiap.meumenu.core.controllers.restaurante.*;
import com.postechfiap.meumenu.core.controllers.restaurante.item.AdicionarItemCardapioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.AtualizarItemCardapioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.BuscarItemCardapioPorIdInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.DeletarItemCardapioInputPort;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AdicionarItemCardapioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarItemCardapioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.AtualizarRestauranteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarRestauranteRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.CadastrarRestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.ItemCardapioResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.RestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.*;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item.AdicionarItemCardapioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item.AtualizarItemCardapioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item.BuscarItemCardapioPorIdPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.item.DeletarItemCardapioPresenter;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteResource {

    private final CadastrarRestauranteInputPort cadastrarRestauranteInputPort;
    private final CadastrarRestaurantePresenter cadastrarRestaurantePresenter;
    private final BuscarTodosRestaurantesInputPort buscarTodosRestaurantesInputPort;
    private final BuscarTodosRestaurantesPresenter buscarTodosRestaurantesPresenter;
    private final BuscarRestaurantePorIdInputPort buscarRestaurantePorIdInputPort;
    private final BuscarRestaurantePorIdPresenter buscarRestaurantePorIdPresenter;
    private final AtualizarRestauranteInputPort atualizarRestauranteInputPort;
    private final AtualizarRestaurantePresenter atualizarRestaurantePresenter;
    private final DeletarRestauranteInputPort deletarRestauranteInputPort;
    private final DeletarRestaurantePresenter deletarRestaurantePresenter;
    private final AdicionarItemCardapioInputPort adicionarItemCardapioInputPort;
    private final AdicionarItemCardapioPresenter adicionarItemCardapioPresenter;
    private final DeletarItemCardapioInputPort deletarItemCardapioInputPort;
    private final DeletarItemCardapioPresenter deletarItemCardapioPresenter;
    private final AtualizarItemCardapioInputPort atualizarItemCardapioInputPort;
    private final AtualizarItemCardapioPresenter atualizarItemCardapioPresenter;
    private final BuscarItemCardapioPorIdInputPort buscarItemCardapioPorIdInputPort;
    private final BuscarItemCardapioPorIdPresenter buscarItemCardapioPorIdPresenter;

    @Operation(
            summary = "Realiza o cadastro de um novo restaurante",
            description = "Este endpoint permite que um Proprietário logado cadastre um novo restaurante no sistema.",
            tags = {"Restaurante"}
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
            description = "Este endpoint retorna uma lista de todos os restaurantes cadastrados no sistema, sem os itens do cardápio.",
            tags = {"Restaurante"}
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
            summary = "Busca um restaurante por ID",
            description = "Este endpoint retorna os detalhes completos de um restaurante específico pelo seu ID, incluindo itens do cardápio.",
            tags = {"Restaurante"}
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarRestaurantePorId(@PathVariable UUID id) {
        buscarRestaurantePorIdInputPort.execute(id);
        return ResponseEntity.ok(buscarRestaurantePorIdPresenter.getViewModel());
    }

    @Operation(
            summary = "Atualiza um restaurante por ID",
            description = "Este endpoint permite que o Proprietário de um restaurante atualize seus dados. Apenas o proprietário do restaurante pode realizar esta operação.",
            tags = {"Restaurante"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(
            @PathVariable UUID id,
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

        atualizarRestauranteInputPort.execute(id, requestDTO.toInputModel(proprietarioLogadoId));
        return ResponseEntity.ok(atualizarRestaurantePresenter.getViewModel());
    }

    @Operation(
            summary = "Deleta um restaurante por ID",
            description = "Este endpoint permite que o Proprietário de um restaurante o exclua. Apenas o proprietário do restaurante pode realizar esta operação.",
            tags = {"Restaurante"}
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

    @Operation(
            summary = "Adiciona um item ao cardápio de um restaurante",
            description = "Este endpoint permite que o Proprietário de um restaurante adicione um novo item ao cardápio. O proprietário logado deve ser o dono do restaurante.",
            tags = {"Item do Cardápio"}
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
            summary = "Deleta um item do cardápio de um restaurante",
            description = "Este endpoint permite que o Proprietário de um restaurante exclua um item específico do cardápio. Apenas o proprietário do restaurante pode realizar esta operação.",
            tags = {"Item do Cardápio"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @DeleteMapping("/{restauranteId}/itens/{itemId}")
    public ResponseEntity<Void> deletarItemCardapio(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioLogadoId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) {
                proprietarioLogadoId = ((com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem deletar itens do cardápio. Tipo de principal inesperado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }

        deletarItemCardapioInputPort.execute(restauranteId, itemId, proprietarioLogadoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualiza um item do cardápio de um restaurante",
            description = "Este endpoint permite que o Proprietário de um restaurante atualize um item específico do cardápio. Apenas o proprietário do restaurante pode realizar esta operação.",
            tags = {"Item do Cardápio"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PROPRIETARIOENTITY')")
    @PutMapping("/{restauranteId}/itens/{itemId}")
    public ResponseEntity<ItemCardapioResponseDTO> atualizarItemCardapio(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId,
            @RequestBody @Valid AtualizarItemCardapioRequestDTO requestDTO
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID proprietarioLogadoId;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails instanceof com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) {
                proprietarioLogadoId = ((com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity) userDetails).getId();
            } else {
                throw new BusinessException("Apenas usuários do tipo Proprietário podem atualizar itens do cardápio. Tipo de principal inesperado.");
            }
        } else {
            throw new BusinessException("Usuário não autenticado. Acesso negado.");
        }

        atualizarItemCardapioInputPort.execute(restauranteId, itemId, requestDTO.toInputModel(), proprietarioLogadoId);
        return ResponseEntity.ok(atualizarItemCardapioPresenter.getViewModel());
    }

    @Operation(
            summary = "Busca um item do cardápio de um restaurante por ID",
            description = "Este endpoint retorna os detalhes de um item específico do cardápio de um restaurante.",
            tags = {"Item do Cardápio"}
    )
    @GetMapping("/{restauranteId}/itens/{itemId}")
    public ResponseEntity<ItemCardapioResponseDTO> buscarItemCardapioPorId(
            @PathVariable UUID restauranteId,
            @PathVariable UUID itemId
    ) {
        buscarItemCardapioPorIdInputPort.execute(restauranteId, itemId);
        return ResponseEntity.ok(buscarItemCardapioPorIdPresenter.getViewModel());
    }
}