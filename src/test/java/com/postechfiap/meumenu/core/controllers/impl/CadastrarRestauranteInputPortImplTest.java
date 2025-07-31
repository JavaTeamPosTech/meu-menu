package com.postechfiap.meumenu.core.controllers.impl;

import com.postechfiap.meumenu.core.controllers.restaurante.impl.CadastrarRestauranteInputPortImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.CadastrarRestauranteUseCase;
import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.endereco.EnderecoRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CadastrarRestauranteInputPortImplTest {

    @Mock
    private CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @InjectMocks
    private CadastrarRestauranteInputPortImpl cadastrarRestauranteInputPort;

    private CadastrarRestauranteInputModel inputModel;
    private UUID proprietarioId;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();

        // Criar InputModels aninhados
        EnderecoRestauranteInputModel enderecoInput = new EnderecoRestauranteInputModel(
                "SP", "Sao Paulo", "Centro", "Rua Principal", 123, "Sala 1", "01000-000");
        TipoCozinhaInputModel tipoCozinhaInput = new TipoCozinhaInputModel("Italiana");
        HorarioFuncionamentoInputModel horarioInput = new HorarioFuncionamentoInputModel(
                LocalTime.of(9, 0), LocalTime.of(18, 0), DiaSemanaEnum.SEGUNDA_FEIRA);

        // CadastrarRestauranteInputModel completo
        inputModel = new CadastrarRestauranteInputModel(
                proprietarioId,
                "00.000.000/0001-00", // CNPJ
                "Restaurante Teste Ltda",
                "Delicias da Esquina",
                "123456789", // IE
                "11987654321", // Telefone
                enderecoInput,
                List.of(tipoCozinhaInput),
                List.of(horarioInput)
        );
    }

    @Test
    @DisplayName("Deve delegar a execução para o CadastrarRestauranteUseCase corretamente")
    void shouldDelegateExecutionToCadastrarRestauranteUseCase() {
        // Executa o método do InputPortImpl
        assertDoesNotThrow(() -> cadastrarRestauranteInputPort.execute(inputModel));

        // Verifica se o método execute do UseCase foi chamado exatamente uma vez
        // com o argumento correto.
        verify(cadastrarRestauranteUseCase, times(1)).execute(
                any(CadastrarRestauranteInputModel.class) // Pode usar eq(inputModel) se equals/hashCode forem confiáveis
        );
    }
}