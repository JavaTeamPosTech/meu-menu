package com.postechfiap.meumenu.dtos.response;

import java.util.List;
import java.util.UUID;

public record AtualizarRestauranteResponseDTO(
        UUID id,
        String cnpj,
        String razaoSocial,
        String nomeFantasia,
        String inscricaoEstadual,
        String telefoneComercial,
        EnderecoRestauranteResponseDTO endereco,
        List<TipoCozinhaResponseDTO> tiposCozinha,
        List<HorarioFuncionamentoResponseDTO> horariosFuncionamento
) {}