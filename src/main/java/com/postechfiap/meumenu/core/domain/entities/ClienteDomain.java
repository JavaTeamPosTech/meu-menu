package com.postechfiap.meumenu.core.domain.entities;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ClienteDomain extends UsuarioDomain {

    private String cpf;

    private LocalDate dataNascimento;

    private GeneroEnum genero;

    private String telefone;

    private Set<TiposComidaEnum> preferenciasAlimentares = new HashSet<>();

    private Set<AlergiaAlimentarEnum> alergias = new HashSet<>();

    private MetodoPagamentoEnum metodoPagamentoPreferido;

    private Date ultimoPedido;

    private Integer saldoPontos;

    private Boolean clienteVip;

    private Integer avaliacoesFeitas;

    private Boolean notificacoesAtivas;

    public ClienteDomain(UUID id, String cpf, LocalDate dataNascimento, GeneroEnum genero, String telefone,
                         Set<TiposComidaEnum> preferenciasAlimentares, Set<AlergiaAlimentarEnum> alergias,
                         MetodoPagamentoEnum metodoPagamentoPreferido, Boolean notificacoesAtivas,
                         Boolean clienteVip, Integer saldoPontos, Integer avaliacoesFeitas, Date ultimoPedido,
                         String nome, String email, String login, String senhaCriptografada) {

        super(id, nome, email, login, senhaCriptografada);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.telefone = telefone;
        this.preferenciasAlimentares = preferenciasAlimentares != null
                ? new HashSet<>(preferenciasAlimentares)
                : new HashSet<>();
        this.alergias = alergias != null
                ? new HashSet<>(alergias)
                : new HashSet<>();
        this.metodoPagamentoPreferido = metodoPagamentoPreferido;
        this.clienteVip = clienteVip;
        this.notificacoesAtivas = notificacoesAtivas;
        this.saldoPontos = saldoPontos;
        this.avaliacoesFeitas = avaliacoesFeitas;
        this.ultimoPedido = ultimoPedido;
    }

    public ClienteDomain(
            UUID id,
             String cpf,
             LocalDate dataNascimento,
             GeneroEnum genero,
             String telefone,
             Set<TiposComidaEnum> preferenciasAlimentares,
             Set<AlergiaAlimentarEnum> alergias,
             MetodoPagamentoEnum metodoPagamentoPreferido,
             Boolean notificacoesAtivas,
             String nome,
             String email,
             String login,
             String senhaCriptografada) {

        super(id, nome, email, login, senhaCriptografada);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.telefone = telefone;
        this.preferenciasAlimentares = preferenciasAlimentares != null
                ? new HashSet<>(preferenciasAlimentares)
                : new HashSet<>();
        this.alergias = alergias != null
                ? new HashSet<>(alergias)
                : new HashSet<>();
        this.metodoPagamentoPreferido = metodoPagamentoPreferido;
        this.notificacoesAtivas = notificacoesAtivas;
        this.clienteVip = false;
        this.saldoPontos = 0;
        this.avaliacoesFeitas = 0;
        this.ultimoPedido = null;
    }
}
