package com.postechfiap.meumenu.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.dtos.request.AtualizarClienteRequestDTO;
import com.postechfiap.meumenu.dtos.request.CadastrarClienteRequestDTO;
import com.postechfiap.meumenu.dtos.request.EnderecoRequestDTO;
import com.postechfiap.meumenu.dtos.request.LoginRequestDTO;
import com.postechfiap.meumenu.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.data.repositories.ClienteSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ClienteResourceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired private ClienteGateway clienteGateway;
    @Autowired private UsuarioGateway usuarioGateway;
    @Autowired private PasswordService passwordService;
    @Autowired private ClienteSpringRepository clienteSpringRepository;

    private CadastrarClienteRequestDTO cadastrarClienteRequestDTO;
    private ClienteDomain clienteParaAtualizarOuDeletar; // Cliente existente para PUT/DELETE/GET por ID
    private String rawPassword;
    private String jwtToken;

    private static int counter = 0;

    @BeforeEach
    void setUp() throws Exception {
        counter++;
        // Gerar um sufixo único para cada execução do setUp
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8) + counter; // Usar UUID para maior unicidade

        rawPassword = "password" + uniqueSuffix;
        String hashedPassword = passwordService.encryptPassword(rawPassword);

        // 1. Preparar DTO de cadastro para o teste POST
        EnderecoRequestDTO enderecoRequest = new EnderecoRequestDTO(
                "SP", "Sao Paulo", "Centro", "Rua Teste " + uniqueSuffix, 100 + counter, "Apto " + counter, "01000-" + String.format("%03d", counter)
        );
        cadastrarClienteRequestDTO = new CadastrarClienteRequestDTO(
                "Cliente Teste " + counter,
                "111222321" + String.format("%02d", counter),
                "cliente" + counter + "@email.com",
                "login_test" + counter ,
                LocalDate.of(1990, 1, 1),
                GeneroEnum.MASCULINO,
                "119876543" + String.format("%02d", counter),
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX, true,
                rawPassword,
                List.of(enderecoRequest)

        );

        // 2. Preparar um cliente já existente no DB para testes de GET, PUT, DELETE
        String loginExistente = "login_existente_" + uniqueSuffix;
        String emailExistente = "existente_" + uniqueSuffix + "@email.com";
        String cpfExistente = "999888777" + String.format("%02d", counter) + uniqueSuffix.substring(0,2); // CPF mais único

        ClienteDomain tempClienteDomain = new ClienteDomain(
                cadastrarClienteRequestDTO.nome() + "_existente",
                cadastrarClienteRequestDTO.email().replace("@", "_existente@"),
                loginExistente,
                hashedPassword,
                cpfExistente,
                LocalDate.of(1985, 10, 20), GeneroEnum.FEMININO,
                "119777766" + String.format("%02d", counter),
                new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.DEBITO,
                true, false);
        clienteParaAtualizarOuDeletar = clienteGateway.cadastrarCliente(tempClienteDomain);

        // 3. Obter JWT para o clienteParaAtualizarOuDeletar
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                clienteParaAtualizarOuDeletar.getLogin(), rawPassword);
        String loginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO loginResponse = objectMapper.readValue(loginResponseContent, LoginResponseDTO.class);
        jwtToken = loginResponse.token();
    }

    // --- Testes para POST /clientes (Cadastrar Cliente) ---
    @Test
    @DisplayName("POST /clientes deve cadastrar um cliente com sucesso")
    void postCliente_shouldCadastrarClienteSuccessfully() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastrarClienteRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(cadastrarClienteRequestDTO.nome()))
                .andExpect(jsonPath("$.email").value(cadastrarClienteRequestDTO.email()))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        assertTrue(usuarioGateway.existsByLogin(cadastrarClienteRequestDTO.login()));
    }

    @Test
    @DisplayName("POST /clientes deve falhar com dados de validação inválidos")
    void postCliente_shouldFailWithInvalidValidationData() throws Exception {
        CadastrarClienteRequestDTO invalidRequest = new CadastrarClienteRequestDTO(
                "", // Nome vazio
                "123", // CPF inválido
                "email_invalido", // Email inválido
                "log", // Login muito curto
                LocalDate.of(2030,1,1), // Data futura
                null, // Gênero nulo
                "123", // Telefone curto
                null, null, null, null, // Set e enum vazios ou nulos
                "",
                null
        );

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").isArray());
    }

    @Test
    @DisplayName("POST /clientes deve falhar se login já cadastrado")
    void postCliente_shouldFailIfLoginAlreadyExists() throws Exception {
        // Tentar cadastrar com login que já existe (o do clienteParaAtualizarOuDeletar)
        CadastrarClienteRequestDTO duplicateLoginRequest = new CadastrarClienteRequestDTO(
                "Outro Cliente",
                "999999999" + String.format("%02d", counter) + "1", // CPF - ÚNICO para este teste
                "outro_duplicado@email.com", // Email
                clienteParaAtualizarOuDeletar.getLogin(), // Login - Este é o que causa o conflito
                LocalDate.of(1991, 2, 2),
                GeneroEnum.FEMININO,
                "11987654399",
                new HashSet<>(),
                new HashSet<>(),
                MetodoPagamentoEnum.DEBITO,
                true,
                "senha456",
                Collections.emptyList()
        );

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateLoginRequest)))
                .andExpect(status().isBadRequest());
    }

    // --- Testes para GET /clientes/{id} (Buscar Cliente por ID) ---
    @Test
    @DisplayName("GET /clientes/{id} deve buscar cliente por ID com sucesso se autenticado como o próprio cliente")
    void getClienteById_shouldReturnClienteSuccessfullyWhenAuthenticatedAsSelf() throws Exception {
        mockMvc.perform(get("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteParaAtualizarOuDeletar.getId().toString()))
                .andExpect(jsonPath("$.nome").value(clienteParaAtualizarOuDeletar.getNome()));
    }

    @Test
    @DisplayName("GET /clientes/{id} deve retornar 403 Forbidden se autenticado como outro cliente")
    void getClienteById_shouldReturnForbiddenWhenAuthenticatedAsOtherClient() throws Exception {
        // Criar outro cliente e obter seu token
        String otherRawPassword = "other_password" + UUID.randomUUID().toString().substring(0, 5); // Sufixo único
        ClienteDomain otherCliente = new ClienteDomain(
                "Other Client " + counter, "other_get" + counter + "@email.com", "other_login_get" + counter, passwordService.encryptPassword(otherRawPassword),
                "000000000" + String.format("%02d", counter) + "2", // CPF - ÚNICO
                LocalDate.of(1992, 3, 3), GeneroEnum.OUTRO, "11911112222", new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.PIX, true, false);
        clienteGateway.cadastrarCliente(otherCliente);

        LoginRequestDTO otherLoginRequest = new LoginRequestDTO(otherCliente.getLogin(), otherRawPassword);
        String otherLoginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherLoginRequest)))
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO otherLoginResponse = objectMapper.readValue(otherLoginResponseContent, LoginResponseDTO.class);
        String otherJwtToken = otherLoginResponse.token();

        // Tentar acessar o cliente original (clienteParaAtualizarOuDeletar) com o token do OUTRO cliente
        mockMvc.perform(get("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + otherJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    // --- Testes para GET /clientes (Listar todos clientes) ---
    @Test
    @DisplayName("GET /clientes deve listar todos os clientes cadastrados (acesso livre)")
    void getClientes_shouldListAllClients() throws Exception {
        // Cadastrar mais um cliente para garantir que a lista tem mais de 1
        String anotherUniqueSuffix = UUID.randomUUID().toString().substring(0, 8) + (counter + 1);
        ClienteDomain anotherCliente = new ClienteDomain(
                "Another Client " + anotherUniqueSuffix, "another" + anotherUniqueSuffix + "@email.com", "another_login" + anotherUniqueSuffix, passwordService.encryptPassword("pass"),
                "000000000" + String.format("%02d", counter + 1) + "6", // CPF - ÚNICO
                LocalDate.of(1993, 4, 4), GeneroEnum.MASCULINO, "11911113333", new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.PIX, true, false);
        clienteGateway.cadastrarCliente(anotherCliente);

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(2)) // 2 clientes (o do setup + o outro)
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    @DisplayName("GET /clientes deve retornar 204 No Content se não houver clientes")
    void getClientes_shouldReturnNoContentIfNoClients() throws Exception {
        clienteSpringRepository.deleteAll(); // Limpa a tabela antes deste teste

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    // --- Testes para DELETE /clientes/{id} ---
    @Test
    @DisplayName("DELETE /clientes/{id} deve deletar cliente com sucesso se autenticado como o próprio cliente")
    void deleteClienteById_shouldDeleteClienteSuccessfully() throws Exception {
        mockMvc.perform(delete("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cliente com ID " + clienteParaAtualizarOuDeletar.getId() + " excluído com sucesso."));

        // Verificar que o cliente foi removido do DB
        assertFalse(clienteGateway.buscarClientePorId(clienteParaAtualizarOuDeletar.getId()).isPresent());
    }

    @Test
    @DisplayName("DELETE /clientes/{id} deve retornar 403 Forbidden se autenticado como outro cliente")
    void deleteClienteById_shouldReturnForbiddenWhenAuthenticatedAsOtherClient() throws Exception {
        // Criar outro cliente e obter seu token
        String otherRawPassword = "other_password_del" + UUID.randomUUID().toString().substring(0, 5);
        ClienteDomain otherCliente = new ClienteDomain(
                "Other Client Del " + counter, "other_del" + counter + "@email.com", "other_login_del" + counter, passwordService.encryptPassword(otherRawPassword),
                "000000000" + String.format("%02d", counter) + "7", // CPF - ÚNICO
                LocalDate.of(1992, 3, 3), GeneroEnum.OUTRO, "11911112223", new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.PIX, true, false);
        clienteGateway.cadastrarCliente(otherCliente);

        LoginRequestDTO otherLoginRequest = new LoginRequestDTO(otherCliente.getLogin(), otherRawPassword);
        String otherLoginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherLoginRequest)))
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO otherLoginResponse = objectMapper.readValue(otherLoginResponseContent, LoginResponseDTO.class);
        String otherJwtToken = otherLoginResponse.token();


        // Tentar deletar o cliente original (clienteParaAtualizarOuDeletar) com o token do OUTRO cliente
        mockMvc.perform(delete("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + otherJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /clientes/{id} deve retornar 400 Forbidden se autenticado como outro cliente")
    void putClienteById_shouldReturnForbiddenWhenAuthenticatedAsOtherClient() throws Exception {
        // Criar outro cliente e obter seu token
        String otherRawPassword = "other_password_put" + UUID.randomUUID().toString().substring(0, 5);
        ClienteDomain otherCliente = new ClienteDomain(
                "Other Client Put " + counter, "other_put" + counter + "@email.com", "other_login_put" + counter, passwordService.encryptPassword(otherRawPassword),
                "000000000" + String.format("%02d", counter) + "8", // CPF - ÚNICO
                LocalDate.of(1993, 4, 4), GeneroEnum.OUTRO, "11911114444", new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.PIX, true, false);
        clienteGateway.cadastrarCliente(otherCliente);

        LoginRequestDTO otherLoginRequest = new LoginRequestDTO(otherCliente.getLogin(), otherRawPassword);
        String otherLoginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherLoginRequest)))
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO otherLoginResponse = objectMapper.readValue(otherLoginResponseContent, LoginResponseDTO.class);
        String otherJwtToken = otherLoginResponse.token();

        AtualizarClienteRequestDTO updateRequest = new AtualizarClienteRequestDTO(
                "Nome", "email@test.com", "login", "12345678900", LocalDate.now(), GeneroEnum.MASCULINO, "111", null, null, null, true
        );

        // Tentar atualizar o cliente original (clienteParaAtualizarOuDeletar) com o token do OUTRO cliente
        mockMvc.perform(put("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + otherJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /clientes/{id} deve retornar 400 Bad Request se email já existir para outro usuário")
    void putClienteById_shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        ClienteDomain conflictingClient = new ClienteDomain(
                "Conflicting Client " + counter, "conflicting" + counter + "@email.com", "conflicting_login" + counter, passwordService.encryptPassword("pass"),
                "000000000" + String.format("%02d", counter) + "9", // CPF - ÚNICO
                LocalDate.of(1994, 5, 5), GeneroEnum.MASCULINO, "11911115555", new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.PIX, true, false);
        clienteGateway.cadastrarCliente(conflictingClient);

        AtualizarClienteRequestDTO updateRequest = new AtualizarClienteRequestDTO(
                "Nome", conflictingClient.getEmail(), "login", "12345678900", LocalDate.now(), GeneroEnum.MASCULINO, "111", null, null, null, true
        );

        mockMvc.perform(put("/clientes/" + clienteParaAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }
}