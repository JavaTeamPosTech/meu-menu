package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.domain.usecases.restaurante.AtualizarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.endereco.EnderecoRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.time.LocalTime;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarRestauranteInputPortImplTest {

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @InjectMocks
    private AtualizarRestauranteInputPortImpl atualizarRestauranteInputPort;

    private UUID restauranteId;
    private UUID proprietarioLogadoId;
    private AtualizarRestauranteInputModel inputModel;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        proprietarioLogadoId = UUID.randomUUID();

        // Sub-InputModels para o Restaurante
        EnderecoRestauranteInputModel enderecoInput = new EnderecoRestauranteInputModel(
                "SP", "Sao Paulo", "Centro", "Rua Teste", 100, "Complemento", "01000-000");
        TipoCozinhaInputModel tipoCozinhaInput = new TipoCozinhaInputModel("Italiana");
        HorarioFuncionamentoInputModel horarioInput = new HorarioFuncionamentoInputModel(
                LocalTime.of(9,0), LocalTime.of(18,0), DiaSemanaEnum.SEGUNDA_FEIRA);

        inputModel = new AtualizarRestauranteInputModel(
                proprietarioLogadoId, // Este é o proprietarioLogadoId que o UseCase vai usar
                "12345678901234", // CNPJ
                "Razao Social S.A.", // Razao Social
                "Nome Fantasia", // Nome Fantasia
                "ISENTO", // Inscricao Estadual
                "11987654321", // Telefone Comercial
                enderecoInput,
                List.of(tipoCozinhaInput),
                List.of(horarioInput)
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o AtualizarRestauranteUseCase corretamente")
    void shouldDelegateExecutionToAtualizarRestauranteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> atualizarRestauranteInputPort.execute(restauranteId, inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com os argumentos corretos.
        verify(atualizarRestauranteUseCase, times(1)).execute(
                eq(restauranteId),
                any(AtualizarRestauranteInputModel.class)// Pode usar eq(inputModel) se equals/hashCode forem confiáveis
        );
    }
}