package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.CadastrarRestauranteOutputPort;
import com.postechfiap.meumenu.core.dtos.restaurante.CadastrarRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.endereco.EnderecoRestauranteInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.horario.HorarioFuncionamentoInputModel;
import com.postechfiap.meumenu.core.dtos.restaurante.tipocozinha.TipoCozinhaInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.exceptions.ResourceNotFoundException;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.TipoCozinhaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarRestauranteUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private TipoCozinhaGateway tipoCozinhaGateway;
    @Mock
    private CadastrarRestauranteOutputPort cadastrarRestauranteOutputPort;

    @InjectMocks
    private CadastrarRestauranteUseCaseImpl cadastrarRestauranteUseCase;

    private UUID proprietarioId;
    private ProprietarioDomain proprietarioDomain;
    private CadastrarRestauranteInputModel inputModel;
    private RestauranteDomain novoRestauranteMock;

    @BeforeEach
    void setUp() {
        proprietarioId = UUID.randomUUID();
        proprietarioDomain = new ProprietarioDomain(
                proprietarioId, "12345678900", "11987654321", StatusContaEnum.ATIVO,
                "Dono Teste", "dono@teste.com", "dono.login", "hash_senha",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList(), Collections.emptyList()
        );

        EnderecoRestauranteInputModel enderecoInput = new EnderecoRestauranteInputModel(
                "SP", "Sao Paulo", "Centro", "Rua X", 100, "Loja A", "01000-000");

        TipoCozinhaInputModel tipoCozinhaInput = new TipoCozinhaInputModel("Italiana");
        TipoCozinhaDomain tipoCozinhaDomain = new TipoCozinhaDomain(UUID.randomUUID(), "Italiana");

        HorarioFuncionamentoInputModel horarioInput1 = new HorarioFuncionamentoInputModel(
                LocalTime.of(9, 0), LocalTime.of(12, 0), DiaSemanaEnum.SEGUNDA_FEIRA);
        HorarioFuncionamentoInputModel horarioInput2 = new HorarioFuncionamentoInputModel(
                LocalTime.of(14, 0), LocalTime.of(18, 0), DiaSemanaEnum.SEGUNDA_FEIRA);

        inputModel = new CadastrarRestauranteInputModel(
                proprietarioId, "00000000000100", "Razao Social Ltda", "Restaurante Teste",
                "ISENTO", "1122334455",
                enderecoInput,
                List.of(tipoCozinhaInput),
                List.of(horarioInput1, horarioInput2)
        );

        novoRestauranteMock = new RestauranteDomain(
                inputModel.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(),
                proprietarioDomain, // Proprietário associado
                new EnderecoRestauranteDomain(enderecoInput.getEstado(), enderecoInput.getCidade(), enderecoInput.getBairro(),
                        enderecoInput.getRua(), enderecoInput.getNumero(), enderecoInput.getComplemento(), enderecoInput.getCep()),
                List.of(tipoCozinhaDomain), // Tipo Cozinha associado
                List.of(new HorarioFuncionamentoDomain(horarioInput1.getAbre(), horarioInput1.getFecha(), horarioInput1.getDiaSemana()),
                        new HorarioFuncionamentoDomain(horarioInput2.getAbre(), horarioInput2.getFecha(), horarioInput2.getDiaSemana()))
        );
        novoRestauranteMock.setId(UUID.randomUUID()); // Simula ID do restaurante
        novoRestauranteMock.getEndereco().setRestaurante(novoRestauranteMock); // Simula associação bidirecional
        novoRestauranteMock.getHorariosFuncionamento().forEach(h -> h.setRestaurante(novoRestauranteMock)); // Simula associação bidirecional
    }

    @Test
    @DisplayName("Deve cadastrar um novo restaurante com sucesso")
    void shouldCadastrarRestauranteSuccessfully() {
        // Mocks de busca e validações
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));
        when(restauranteGateway.existsByCnpj(anyString())).thenReturn(false);
        when(tipoCozinhaGateway.buscarOuCriarTipoCozinha(anyString()))
                .thenReturn(new TipoCozinhaDomain(UUID.randomUUID(), "Italiana")); // Retorna um tipo de cozinha mockado

        // Mock para o gateway de cadastro de restaurante
        when(restauranteGateway.cadastrarRestaurante(any(RestauranteDomain.class)))
                .thenAnswer(invocation -> {
                    RestauranteDomain restaurantePassed = invocation.getArgument(0);
                    restaurantePassed.setId(UUID.randomUUID()); // Simula ID gerado
                    if (restaurantePassed.getEndereco() != null) {
                        restaurantePassed.getEndereco().setId(UUID.randomUUID());
                    }
                    if (restaurantePassed.getHorariosFuncionamento() != null) {
                        restaurantePassed.getHorariosFuncionamento().forEach(h -> h.setId(UUID.randomUUID()));
                    }
                    return restaurantePassed;
                });

        // Executa o Use Case
        RestauranteDomain result = assertDoesNotThrow(() -> cadastrarRestauranteUseCase.execute(inputModel));

        // Verifica o retorno
        assertNotNull(result);
        assertEquals(inputModel.getNomeFantasia(), result.getNomeFantasia());
        assertNotNull(result.getId());
        assertNotNull(result.getEndereco().getId()); // Endereço deve ter ID
        assertFalse(result.getHorariosFuncionamento().isEmpty());
        assertNotNull(result.getHorariosFuncionamento().get(0).getId()); // Horário deve ter ID
        assertEquals(proprietarioDomain, result.getProprietario()); // Proprietário deve estar associado
        assertEquals(1, result.getTiposCozinha().size()); // Deve ter um tipo de cozinha

        // Verifica as interações com mocks
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(restauranteGateway, times(1)).existsByCnpj(inputModel.getCnpj());
        verify(tipoCozinhaGateway, times(1)).buscarOuCriarTipoCozinha(inputModel.getTiposCozinha().get(0).getNome());
        verify(restauranteGateway, times(1)).cadastrarRestaurante(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, times(1)).presentSuccess(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, never()).presentError(anyString());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o proprietário não for encontrado")
    void shouldThrowResourceNotFoundExceptionWhenProprietarioNotFound() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cadastrarRestauranteUseCase.execute(inputModel));

        assertEquals("Proprietário com ID " + proprietarioId + " não encontrado.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(restauranteGateway, never()).existsByCnpj(anyString()); // Não deve verificar CNPJ
        verify(restauranteGateway, never()).cadastrarRestaurante(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o CNPJ já existir")
    void shouldThrowBusinessExceptionWhenCnpjExists() {
        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));
        when(restauranteGateway.existsByCnpj(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarRestauranteUseCase.execute(inputModel));

        assertEquals("CNPJ '" + inputModel.getCnpj() + "' já cadastrado.", exception.getMessage());
        verify(proprietarioGateway, times(1)).buscarProprietarioPorId(proprietarioId);
        verify(restauranteGateway, times(1)).existsByCnpj(inputModel.getCnpj());
        verify(restauranteGateway, never()).cadastrarRestaurante(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, times(1)).presentError(anyString());
    }

    @Test
    @DisplayName("Deve lançar BusinessException se horário de abertura for igual ou posterior ao de fechamento")
    void shouldThrowBusinessExceptionWhenHorarioAberturaIsAfterOrEqualToFechamento() {
        // Altera o inputModel para ter um horário inválido
        HorarioFuncionamentoInputModel invalidHorario = new HorarioFuncionamentoInputModel(
                LocalTime.of(12, 0), LocalTime.of(9, 0), DiaSemanaEnum.SEGUNDA_FEIRA);
        inputModel = new CadastrarRestauranteInputModel(
                proprietarioId, "00000000000200", "Razao Social Ltda", "Restaurante Invalido",
                "ISENTO", "1122334466",
                new EnderecoRestauranteInputModel("SP", "SP", "Bairro", "Rua", 1, null, "00000-000"),
                Collections.emptyList(),
                List.of(invalidHorario)
        );

        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));
        when(restauranteGateway.existsByCnpj(anyString())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarRestauranteUseCase.execute(inputModel));

        assertEquals("Horário de abertura ('" + invalidHorario.getAbre() + "') deve ser anterior ao horário de fechamento ('" + invalidHorario.getFecha() + "') para o dia " + invalidHorario.getDiaSemana() + ".", exception.getMessage());
        verify(cadastrarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, never()).presentError(anyString()); // HorarioValidation não notifica Presenter
        // A BusinessException é lançada DIRETAMENTE pela validação de horário
    }

    @Test
    @DisplayName("Deve lançar BusinessException se horários sobrepostos para o mesmo dia")
    void shouldThrowBusinessExceptionWhenHorariosOverlap() {
        // Altera o inputModel para ter horários sobrepostos
        HorarioFuncionamentoInputModel horario1 = new HorarioFuncionamentoInputModel(
                LocalTime.of(9, 0), LocalTime.of(13, 0), DiaSemanaEnum.TERCA_FEIRA);
        HorarioFuncionamentoInputModel horario2 = new HorarioFuncionamentoInputModel(
                LocalTime.of(12, 0), LocalTime.of(17, 0), DiaSemanaEnum.TERCA_FEIRA); // Sobreposto

        inputModel = new CadastrarRestauranteInputModel(
                proprietarioId, "00000000000300", "Razao Social X", "Restaurante Com Horario",
                "ISENTO", "1122334477",
                new EnderecoRestauranteInputModel("SP", "SP", "Bairro", "Rua", 1, null, "00000-000"),
                Collections.emptyList(),
                List.of(horario1, horario2)
        );

        when(proprietarioGateway.buscarProprietarioPorId(proprietarioId)).thenReturn(Optional.of(proprietarioDomain));
        when(restauranteGateway.existsByCnpj(anyString())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> cadastrarRestauranteUseCase.execute(inputModel));

        assertTrue(exception.getMessage().contains("Horários sobrepostos para o dia TERCA_FEIRA"));
        verify(cadastrarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
        verify(cadastrarRestauranteOutputPort, never()).presentError(anyString()); // HorarioValidation não notifica Presenter
    }
}