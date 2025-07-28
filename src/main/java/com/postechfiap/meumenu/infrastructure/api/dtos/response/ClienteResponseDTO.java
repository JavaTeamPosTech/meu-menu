package com.postechfiap.meumenu.infrastructure.api.dtos.response;

import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Schema(description = "DTO para resposta com detalhes completos de um Cliente")
public record ClienteResponseDTO(
        @Schema(description = "ID único do cliente")
        UUID id,

        @Schema(description = "Nome completo do cliente")
        String nome,

        @Schema(description = "CPF do cliente (apenas números)")
        String cpf,

        @Schema(description = "Data de nascimento do cliente")
        LocalDate dataNascimento,

        @Schema(description = "Endereço de e-mail do cliente")
        String email,

        @Schema(description = "Login do cliente")
        String login,

        @Schema(description = "Telefone do cliente")
        String telefone,

        @Schema(description = "Gênero do cliente")
        GeneroEnum genero,

        @Schema(description = "Preferências alimentares do cliente")
        Set<TiposComidaEnum> preferenciasAlimentares,

        @Schema(description = "Alergias alimentares do cliente")
        Set<AlergiaAlimentarEnum> alergias,

        @Schema(description = "Método de pagamento preferido do cliente")
        MetodoPagamentoEnum metodoPagamentoPreferido,

        @Schema(description = "Data do último pedido do cliente")
        LocalDateTime ultimoPedido,

        @Schema(description = "Saldo de pontos de fidelidade do cliente")
        Integer saldoPontos,

        @Schema(description = "Indica se o cliente é VIP")
        Boolean clienteVip,

        @Schema(description = "Número de avaliações feitas pelo cliente")
        Integer avaliacoesFeitas,

        @Schema(description = "Indica se o cliente recebe notificações")
        Boolean notificacoesAtivas,

        @Schema(description = "Data de criação do registro do cliente")
        LocalDateTime dataCriacao,

        @Schema(description = "Data da última atualização do registro do cliente")
        LocalDateTime dataAtualizacao,

        @Schema(description = "Lista de endereços do cliente")
        List<EnderecoResponseDTO> enderecos
) {
}