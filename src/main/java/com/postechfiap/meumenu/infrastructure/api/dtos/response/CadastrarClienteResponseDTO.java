package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarClienteResponseDTO {
    private UUID id;
    private String nome;
    private String email;
    private String login;
    private LocalDateTime dataCriacao;
    private String message;
    private String status;
}