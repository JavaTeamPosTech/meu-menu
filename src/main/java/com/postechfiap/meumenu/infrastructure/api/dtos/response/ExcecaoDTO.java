package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcecaoDTO {
    private String mensagem;
    private Integer status;
    private LocalDateTime timestamp;
    private List<ErroValidacaoDTO> erros;
}