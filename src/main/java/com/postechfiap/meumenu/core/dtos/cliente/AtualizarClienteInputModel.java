package com.postechfiap.meumenu.core.dtos.cliente;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarClienteInputModel {
    private UUID id;
    private String nome;
    private String email;
    private String login;
    private String cpf;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private String telefone;
    private Set<TiposComidaEnum> preferenciasAlimentares;
    private Set<AlergiaAlimentarEnum> alergias;
    private MetodoPagamentoEnum metodoPagamentoPreferido;
    private Boolean notificacoesAtivas;
}