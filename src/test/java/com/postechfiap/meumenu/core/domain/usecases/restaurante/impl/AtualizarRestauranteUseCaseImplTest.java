package com.postechfiap.meumenu.core.domain.usecases.restaurante.impl;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.presenters.AtualizarRestauranteOutputPort;
import com.postechfiap.meumenu.core.dtos.restaurante.AtualizarRestauranteInputModel;
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
import org.mockito.ArgumentCaptor;
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
class AtualizarRestauranteUseCaseImplTest {

    @Mock
    private RestauranteGateway restauranteGateway;
    @Mock
    private ProprietarioGateway proprietarioGateway;
    @Mock
    private TipoCozinhaGateway tipoCozinhaGateway;
    @Mock
    private AtualizarRestauranteOutputPort atualizarRestauranteOutputPort;

    @InjectMocks
    private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCase;

    private UUID restauranteId;
    private UUID proprietarioId;
    private RestauranteDomain restauranteExistente;
    private AtualizarRestauranteInputModel inputModel;
    private ProprietarioDomain proprietarioDomain;
    private EnderecoRestauranteInputModel enderecoInput;
    private TipoCozinhaInputModel tipoCozinhaInput;
    private TipoCozinhaDomain tipoCozinhaDomain;
    private HorarioFuncionamentoInputModel horarioInput;

    @BeforeEach
    void setUp() {
        restauranteId = UUID.randomUUID();
        proprietarioId = UUID.randomUUID();

        // Proprietário Mock para o Restaurante
        proprietarioDomain = new ProprietarioDomain(
                proprietarioId, "11122233344", "11988887777", StatusContaEnum.ATIVO,
                "Dono Teste", "dono@teste.com", "dono.login", "hash_senha",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );

        // Endereço Existente do Restaurante
        EnderecoRestauranteDomain enderecoExistente = new EnderecoRestauranteDomain(
                UUID.randomUUID(), "SP", "Sao Paulo", "Centro", "Rua A", 100, "Loja A", "01000-000", null // restaurante will be set later
        );

        // Tipos de Cozinha Existentes
        TipoCozinhaDomain tipoCozinhaExistente = new TipoCozinhaDomain(UUID.randomUUID(), "Brasileira");

        // Horários de Funcionamento Existentes
        HorarioFuncionamentoDomain horarioExistente = new HorarioFuncionamentoDomain(
                UUID.randomUUID(), LocalTime.of(8,0), LocalTime.of(18,0), DiaSemanaEnum.SEGUNDA_FEIRA, null
        );

        // Restaurante Existente (como viria do banco)
        restauranteExistente = new RestauranteDomain(
                restauranteId, "00000000000100", "Original Ltda", "Original Restaurante",
                "ISENTO", "1122334455", proprietarioDomain,
                enderecoExistente, Collections.singletonList(tipoCozinhaExistente),
                Collections.singletonList(horarioExistente), Collections.emptyList() // Itens Cardapio vazios
        );
        enderecoExistente.setRestaurante(restauranteExistente);
        horarioExistente.setRestaurante(restauranteExistente);

        // Input Models para atualização
        enderecoInput = new EnderecoRestauranteInputModel("RJ", "Rio", "Copacabana", "Rua B", 200, "Ap 1", "20000-000");
        tipoCozinhaInput = new TipoCozinhaInputModel("Italiana"); // Novo tipo de cozinha
        horarioInput = new HorarioFuncionamentoInputModel(LocalTime.of(9,0), LocalTime.of(17,0), DiaSemanaEnum.TERCA_FEIRA); // Novo horario

        inputModel = new AtualizarRestauranteInputModel(
                proprietarioId, // proprietarioLogadoId (passado aqui para o inputModel para ser usado no UseCase)
                "00000000000100", // CNPJ (mesmo, sem alteração)
                "Atualizada Ltda", "Nome Fantasia Atualizado", "IE_NOVA", "1199887766",
                enderecoInput, // Novo endereço
                List.of(tipoCozinhaInput), // Nova lista de tipos de cozinha
                List.of(horarioInput) // Nova lista de horários
        );

    }

    @Test
    @DisplayName("Deve atualizar um restaurante com sucesso e refletir as mudanças")
    void shouldUpdateRestauranteSuccessfully() {
        // Mocks
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));
        when(tipoCozinhaGateway.buscarOuCriarTipoCozinha(anyString()))
                .thenReturn(new TipoCozinhaDomain(UUID.randomUUID(), tipoCozinhaInput.getNome())); // Mocka a criação/busca de tipo de cozinha

        // Captura o restaurante que é passado para o gateway.atualizarRestaurante
        ArgumentCaptor<RestauranteDomain> restauranteCaptor = ArgumentCaptor.forClass(RestauranteDomain.class);
        when(restauranteGateway.atualizarRestaurante(restauranteCaptor.capture()))
                .thenAnswer(invocation -> {
                    RestauranteDomain passedRestaurante = invocation.getArgument(0);
                    // Simula que o gateway preencheu IDs para novas sub-entidades (se houver)
                    if (passedRestaurante.getEndereco() != null && passedRestaurante.getEndereco().getId() == null) {
                        passedRestaurante.getEndereco().setId(UUID.randomUUID());
                    }
                    passedRestaurante.getHorariosFuncionamento().forEach(h -> { if (h.getId() == null) h.setId(UUID.randomUUID()); });
                    return passedRestaurante; // Retorna o objeto com IDs simulados
                });

        // Executa o Use Case
        RestauranteDomain result = assertDoesNotThrow(() -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        // Verifica o retorno e as mudanças no objeto retornado
        assertNotNull(result);
        assertEquals(inputModel.getNomeFantasia(), result.getNomeFantasia());
        assertEquals(inputModel.getRazaoSocial(), result.getRazaoSocial());
        assertEquals(inputModel.getTelefoneComercial(), result.getTelefoneComercial());

        // Verifica o endereço
        assertNotNull(result.getEndereco());
        assertEquals(enderecoInput.getCidade(), result.getEndereco().getCidade());
        assertNotNull(result.getEndereco().getId()); // Endereço deve ter ID

        // Verifica Tipos de Cozinha
        assertFalse(result.getTiposCozinha().isEmpty());
        assertEquals(1, result.getTiposCozinha().size());
        assertEquals(tipoCozinhaInput.getNome(), result.getTiposCozinha().get(0).getNome());

        // Verifica Horários de Funcionamento
        assertFalse(result.getHorariosFuncionamento().isEmpty());
        assertEquals(1, result.getHorariosFuncionamento().size());
        assertEquals(horarioInput.getAbre(), result.getHorariosFuncionamento().get(0).getAbre());

        // Verifica as interações com mocks
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(tipoCozinhaGateway, times(1)).buscarOuCriarTipoCozinha(tipoCozinhaInput.getNome());
        verify(restauranteGateway, times(1)).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, times(1)).presentSuccess(any(RestauranteDomain.class));

        // Verifica a associação bidirecional feita no UseCase
        RestauranteDomain capturedRestaurante = restauranteCaptor.getValue();
        assertEquals(capturedRestaurante, capturedRestaurante.getEndereco().getRestaurante());
        capturedRestaurante.getHorariosFuncionamento().forEach(h -> assertEquals(capturedRestaurante, h.getRestaurante()));
    }

    @Test
    @DisplayName("Deve atualizar um restaurante sem alterar CNPJ e pular a validação de CNPJ")
    void shouldUpdateRestauranteSuccessfullyIfCnpjNotChanged() {
        // Mock
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));
        // existsByCnpj NÃO deve ser chamado

        when(tipoCozinhaGateway.buscarOuCriarTipoCozinha(anyString()))
                .thenReturn(new TipoCozinhaDomain(UUID.randomUUID(), tipoCozinhaInput.getNome()));

        when(restauranteGateway.atualizarRestaurante(any(RestauranteDomain.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Apenas retorna o que foi passado

        // InputModel com o mesmo CNPJ do existente
        inputModel = new AtualizarRestauranteInputModel(
                proprietarioId, restauranteExistente.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(), enderecoInput,
                List.of(tipoCozinhaInput), List.of(horarioInput)
        );

        // Executa
        assertDoesNotThrow(() -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        // Verifica
        verify(restauranteGateway, never()).existsByCnpj(anyString()); // existsByCnpj NÃO foi chamado
        verify(restauranteGateway, times(1)).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, times(1)).presentSuccess(any(RestauranteDomain.class));
    }

    @Test
    @DisplayName("Deve remover endereço se inputModel.endereco for null")
    void shouldRemoveAddressIfInputModelAddressIsNull() {
        // InputModel com endereço null
        inputModel = new AtualizarRestauranteInputModel(
                proprietarioId, restauranteExistente.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(), null, // Endereço null
                List.of(tipoCozinhaInput), List.of(horarioInput)
        );

        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));
        when(tipoCozinhaGateway.buscarOuCriarTipoCozinha(anyString()))
                .thenReturn(new TipoCozinhaDomain(UUID.randomUUID(), tipoCozinhaInput.getNome()));

        when(restauranteGateway.atualizarRestaurante(any(RestauranteDomain.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RestauranteDomain result = assertDoesNotThrow(() -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        assertNull(result.getEndereco()); // Endereço deve ser null
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o restaurante não for encontrado para atualização")
    void shouldThrowResourceNotFoundExceptionWhenRestauranteNotFound() {
        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        assertEquals("Restaurante com ID " + restauranteId + " não encontrado para atualização.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se o proprietário logado não for o dono do restaurante")
    void shouldThrowBusinessExceptionWhenProprietarioIsNotOwner() {
        UUID outroProprietarioId = UUID.randomUUID();
        // InputModel com ID de proprietário diferente do dono do restauranteExistente
        inputModel = new AtualizarRestauranteInputModel(
                outroProprietarioId, inputModel.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(), enderecoInput,
                List.of(tipoCozinhaInput), List.of(horarioInput)
        );

        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        assertEquals("Acesso negado. O restaurante com ID " + restauranteId + " não pertence ao proprietário logado.", exception.getMessage());
        verify(restauranteGateway, times(1)).buscarRestaurantePorId(restauranteId);
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se horário de abertura for igual ou posterior ao de fechamento")
    void shouldThrowBusinessExceptionWhenHorarioAberturaIsAfterOrEqualToFechamento() {
        // Altera o inputModel para ter um horário inválido
        HorarioFuncionamentoInputModel invalidHorario = new HorarioFuncionamentoInputModel(
                LocalTime.of(12, 0), LocalTime.of(9, 0), DiaSemanaEnum.SEGUNDA_FEIRA);
        inputModel = new AtualizarRestauranteInputModel(
                proprietarioId, inputModel.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(), enderecoInput,
                List.of(tipoCozinhaInput), List.of(invalidHorario)
        );

        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        assertEquals("Horário de abertura ('" + invalidHorario.getAbre() + "') deve ser anterior ao horário de fechamento ('" + invalidHorario.getFecha() + "') para o dia " + invalidHorario.getDiaSemana() + ".", exception.getMessage());
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException se horários sobrepostos para o mesmo dia")
    void shouldThrowBusinessExceptionWhenHorariosOverlap() {
        // Altera o inputModel para ter horários sobrepostos
        HorarioFuncionamentoInputModel horario1 = new HorarioFuncionamentoInputModel(
                LocalTime.of(9, 0), LocalTime.of(13, 0), DiaSemanaEnum.TERCA_FEIRA);
        HorarioFuncionamentoInputModel horario2 = new HorarioFuncionamentoInputModel(
                LocalTime.of(12, 0), LocalTime.of(17, 0), DiaSemanaEnum.TERCA_FEIRA); // Sobreposto

        inputModel = new AtualizarRestauranteInputModel(
                proprietarioId, inputModel.getCnpj(), inputModel.getRazaoSocial(), inputModel.getNomeFantasia(),
                inputModel.getInscricaoEstadual(), inputModel.getTelefoneComercial(), enderecoInput,
                List.of(tipoCozinhaInput), List.of(horario1, horario2)
        );

        when(restauranteGateway.buscarRestaurantePorId(restauranteId)).thenReturn(Optional.of(restauranteExistente));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> atualizarRestauranteUseCase.execute(restauranteId, inputModel));

        assertTrue(exception.getMessage().contains("Horários sobrepostos para o dia TERCA_FEIRA"));
        verify(restauranteGateway, never()).atualizarRestaurante(any(RestauranteDomain.class));
        verify(atualizarRestauranteOutputPort, never()).presentSuccess(any(RestauranteDomain.class));
    }
}