package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;
import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarRestauranteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.AtualizarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.TipoCozinhaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final ProprietarioGateway proprietarioGateway;
    private final TipoCozinhaGateway tipoCozinhaGateway;
    private final AtualizarRestauranteOutputPort atualizarRestauranteOutputPort;

    @Override
    public RestauranteDomain execute(UUID restauranteId, AtualizarRestauranteInputModel inputModel) {

        Optional<RestauranteDomain> restauranteOptional = restauranteGateway.buscarRestaurantePorId(restauranteId);
        if (restauranteOptional.isEmpty()) {
            throw new ResourceNotFoundException("Restaurante com ID " + restauranteId + " não encontrado para atualização.");
        }
        RestauranteDomain restauranteExistente = restauranteOptional.get();

        if (!restauranteExistente.getProprietario().getId().equals(inputModel.getProprietarioLogadoId())) {
            throw new BusinessException("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.");
        }

        if (!inputModel.getCnpj().equals(restauranteExistente.getCnpj()) && restauranteGateway.existsByCnpj(inputModel.getCnpj())) {
            throw new BusinessException("CNPJ '" + inputModel.getCnpj() + "' já cadastrado por outro restaurante.");
        }

        restauranteExistente.setCnpj(inputModel.getCnpj());
        restauranteExistente.setRazaoSocial(inputModel.getRazaoSocial());
        restauranteExistente.setNomeFantasia(inputModel.getNomeFantasia());
        restauranteExistente.setInscricaoEstadual(inputModel.getInscricaoEstadual());
        restauranteExistente.setTelefoneComercial(inputModel.getTelefoneComercial());

        if (inputModel.getEndereco() != null) {
            EnderecoRestauranteDomain enderecoExistente = restauranteExistente.getEndereco();
            if (enderecoExistente == null) {
                enderecoExistente = new EnderecoRestauranteDomain(
                        inputModel.getEndereco().getEstado(),
                        inputModel.getEndereco().getCidade(),
                        inputModel.getEndereco().getBairro(),
                        inputModel.getEndereco().getRua(),
                        inputModel.getEndereco().getNumero(),
                        inputModel.getEndereco().getComplemento(),
                        inputModel.getEndereco().getCep()
                );
                enderecoExistente.setRestaurante(restauranteExistente);
                restauranteExistente.setEndereco(enderecoExistente);
            } else {
                enderecoExistente.setEstado(inputModel.getEndereco().getEstado());
                enderecoExistente.setCidade(inputModel.getEndereco().getCidade());
                enderecoExistente.setBairro(inputModel.getEndereco().getBairro());
                enderecoExistente.setRua(inputModel.getEndereco().getRua());
                enderecoExistente.setNumero(inputModel.getEndereco().getNumero());
                enderecoExistente.setComplemento(inputModel.getEndereco().getComplemento());
                enderecoExistente.setCep(inputModel.getEndereco().getCep());
            }
        } else {
            if (restauranteExistente.getEndereco() != null) {
                restauranteExistente.setEndereco(null);
            }
        }

        restauranteExistente.getTiposCozinha().clear();
        inputModel.getTiposCozinha().forEach(tipoInput -> {
            TipoCozinhaDomain tipoCozinha = tipoCozinhaGateway.buscarOuCriarTipoCozinha(tipoInput.getNome());
            restauranteExistente.getTiposCozinha().add(tipoCozinha);
        });

        restauranteExistente.getHorariosFuncionamento().clear();
        List<HorarioFuncionamentoDomain> novosHorarios = inputModel.getHorariosFuncionamento().stream()
                .map(horarioInput -> new HorarioFuncionamentoDomain(
                        horarioInput.getAbre(),
                        horarioInput.getFecha(),
                        horarioInput.getDiaSemana()
                ))
                .collect(Collectors.toList());
        validarHorariosFuncionamento(novosHorarios);
        novosHorarios.forEach(horario -> horario.setRestaurante(restauranteExistente));
        restauranteExistente.getHorariosFuncionamento().addAll(novosHorarios);

        RestauranteDomain restauranteAtualizado = restauranteGateway.atualizarRestaurante(restauranteExistente);
        atualizarRestauranteOutputPort.presentSuccess(restauranteAtualizado);

        return restauranteAtualizado;
    }

    private void validarHorariosFuncionamento(List<HorarioFuncionamentoDomain> horarios) {
        for (HorarioFuncionamentoDomain horario : horarios) {
            if (horario.getAbre().isAfter(horario.getFecha()) || horario.getAbre().equals(horario.getFecha())) {
                throw new BusinessException("Horário de abertura ('" + horario.getAbre() + "') deve ser anterior ao horário de fechamento ('" + horario.getFecha() + "') para o dia " + horario.getDiaSemana() + ".");
            }
        }
        horarios.stream()
                .collect(Collectors.groupingBy(HorarioFuncionamentoDomain::getDiaSemana))
                .forEach((dia, horariosDoDia) -> {
                    horariosDoDia.sort((h1, h2) -> h1.getAbre().compareTo(h2.getAbre()));
                    for (int i = 0; i < horariosDoDia.size(); i++) {
                        HorarioFuncionamentoDomain atual = horariosDoDia.get(i);
                        for (int j = i + 1; j < horariosDoDia.size(); j++) {
                            HorarioFuncionamentoDomain proximo = horariosDoDia.get(j);
                            if (atual.getAbre().isBefore(proximo.getFecha()) && atual.getFecha().isAfter(proximo.getAbre())) {
                                throw new BusinessException("Horários sobrepostos para o dia " + dia + ": " +
                                        atual.getAbre() + "-" + atual.getFecha() + " e " +
                                        proximo.getAbre() + "-" + proximo.getFecha() + ".");
                            }
                        }
                    }
                });
    }
}