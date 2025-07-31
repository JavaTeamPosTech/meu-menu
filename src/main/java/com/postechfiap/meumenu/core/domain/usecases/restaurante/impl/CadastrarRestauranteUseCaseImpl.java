package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.CadastrarRestauranteOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.CadastrarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.TipoCozinhaGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CadastrarRestauranteUseCaseImpl implements CadastrarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final ProprietarioGateway proprietarioGateway;
    private final TipoCozinhaGateway tipoCozinhaGateway;
    private final CadastrarRestauranteOutputPort cadastrarRestauranteOutputPort;

    @Override
    public RestauranteDomain execute(CadastrarRestauranteInputModel input) {
        Optional<ProprietarioDomain> proprietarioOptional = proprietarioGateway.buscarProprietarioPorId(input.getProprietarioId());
        if (proprietarioOptional.isEmpty()) {
            throw new ResourceNotFoundException("Proprietário com ID " + input.getProprietarioId() + " não encontrado.");
        }
        ProprietarioDomain proprietario = proprietarioOptional.get();

        if (restauranteGateway.existsByCnpj(input.getCnpj())) {
            cadastrarRestauranteOutputPort.presentError("CNPJ '" + input.getCnpj() + "' já cadastrado.");
            throw new BusinessException("CNPJ '" + input.getCnpj() + "' já cadastrado.");
        }

        EnderecoRestauranteDomain enderecoRestaurante = new EnderecoRestauranteDomain(
                input.getEndereco().getEstado(),
                input.getEndereco().getCidade(),
                input.getEndereco().getBairro(),
                input.getEndereco().getRua(),
                input.getEndereco().getNumero(),
                input.getEndereco().getComplemento(),
                input.getEndereco().getCep()
        );

        List<TipoCozinhaDomain> tiposCozinha = input.getTiposCozinha().stream()
                .map(tipoInput -> tipoCozinhaGateway.buscarOuCriarTipoCozinha(tipoInput.getNome()))
                .collect(Collectors.toList());

        List<HorarioFuncionamentoDomain> horariosFuncionamento = input.getHorariosFuncionamento().stream()
                .map(horarioInput -> new HorarioFuncionamentoDomain(
                        horarioInput.getAbre(),
                        horarioInput.getFecha(),
                        horarioInput.getDiaSemana()
                ))
                .collect(Collectors.toList());

        validarHorariosFuncionamento(horariosFuncionamento);

        RestauranteDomain novoRestaurante = new RestauranteDomain(
                input.getCnpj(),
                input.getRazaoSocial(),
                input.getNomeFantasia(),
                input.getInscricaoEstadual(),
                input.getTelefoneComercial(),
                proprietario,
                enderecoRestaurante,
                tiposCozinha,
                horariosFuncionamento
        );

        enderecoRestaurante.setRestaurante(novoRestaurante);
        horariosFuncionamento.forEach(horario -> horario.setRestaurante(novoRestaurante));
        RestauranteDomain restauranteSalvo = restauranteGateway.cadastrarRestaurante(novoRestaurante);
        cadastrarRestauranteOutputPort.presentSuccess(restauranteSalvo);
        return restauranteSalvo;
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