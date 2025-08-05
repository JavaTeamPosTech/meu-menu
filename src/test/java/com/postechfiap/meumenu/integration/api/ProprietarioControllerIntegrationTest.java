package com.postechfiap.meumenu.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.CadastrarProprietarioRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.EnderecoRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.LoginRequestDTO;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.data.repositories.ProprietarioSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class ProprietarioControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProprietarioGateway proprietarioGateway;

    @Autowired
    private UsuarioGateway usuarioGateway;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ProprietarioSpringRepository proprietarioSpringRepository;

    private CadastrarProprietarioRequestDTO cadastrarProprietarioRequestDTO;
    private ProprietarioDomain proprietarioParaBuscarAtualizarOuDeletar;
    private String rawPassword;
    private String jwtToken;

    private static int counter = 0;

    @BeforeEach
    void setUp() throws Exception {
        counter++;
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8) + counter;

        rawPassword = "password" + uniqueSuffix;
        String hashedPassword = passwordService.encryptPassword(rawPassword);

        EnderecoRequestDTO enderecoRequest = new EnderecoRequestDTO(
                "SP", "São Paulo", "Centro", "Rua Teste " + uniqueSuffix, 100 + counter, "Apto " + counter, "01000-" + String.format("%03d", counter)
        );

        cadastrarProprietarioRequestDTO = new CadastrarProprietarioRequestDTO(
                "Proprietário Teste " + counter,
                "proprietario" + counter + "@email.com",
                "proprietario" + counter,
                "SenhaForte123!",
                "123456789" + String.format("%02d", counter),
                "55119876543" + String.format("%02d", counter),
                List.of(enderecoRequest)
        );

        // 2. Preparar um Proprietario  já existente no DB para testes de GET, PUT, DELETE
        String loginExistente = "login_existente_" + uniqueSuffix;

        ProprietarioDomain tempProprietarioDomain = new ProprietarioDomain(
                UUID.randomUUID(),
                "987654321" + String.format("%02d", counter),
                cadastrarProprietarioRequestDTO.whatsapp(),
                StatusContaEnum.ATIVO,
                cadastrarProprietarioRequestDTO.nome() + "_existente",
                cadastrarProprietarioRequestDTO.email().replace("@", "_existente@"),
                loginExistente,
                hashedPassword,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new EnderecoDomain(
                        enderecoRequest.estado(),
                        enderecoRequest.cidade(),
                        enderecoRequest.bairro(),
                        enderecoRequest.rua(),
                        enderecoRequest.numero(),
                        enderecoRequest.complemento(),
                        enderecoRequest.cep()
                )),
                new ArrayList<>());

        proprietarioParaBuscarAtualizarOuDeletar = proprietarioGateway.cadastrarProprietario(tempProprietarioDomain);

        // 3. Obter JWT para o proprietarioParaAtualizarOuDeletar
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                proprietarioParaBuscarAtualizarOuDeletar.getLogin(), rawPassword);
        String loginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO loginResponse = objectMapper.readValue(loginResponseContent, LoginResponseDTO.class);
        jwtToken = loginResponse.token();
    }

    @Test
    @DisplayName("POST /proprietarios deve cadastrar um proprietário com sucesso")
    void postProprietario_deveCadastrarComSucesso() throws Exception {
        mockMvc.perform(post("/proprietarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastrarProprietarioRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(cadastrarProprietarioRequestDTO.nome()))
                .andExpect(jsonPath("$.email").value(cadastrarProprietarioRequestDTO.email()))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        assertTrue(usuarioGateway.existsByLogin(cadastrarProprietarioRequestDTO.login()));
    }

    @Test
    @DisplayName("POST /clientes deve falhar com dados de validação inválidos")
    void postProprietario_shouldFailWithInvalidValidationData() throws Exception {
        CadastrarProprietarioRequestDTO invalidRequest = new CadastrarProprietarioRequestDTO(
                "", // Nome vazio
                "email_invalido", // Email inválido
                "log", // Login muito curto
                "senha", // Email inválido
                "1231231", // CPF
                "123456789", // WhatsApp inválido
                null
        );
        mockMvc.perform(post("/proprietarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").isArray());
    }

    @Test
    @DisplayName("POST /clientes deve falhar se login já cadastrado")
    void postCliente_shouldFailIfLoginAlreadyExists() throws Exception {
        CadastrarProprietarioRequestDTO duplicateLoginRequest = new CadastrarProprietarioRequestDTO(
                "Proprietário Teste " + counter,
                "proprietario" + counter + "@email.com",
                proprietarioParaBuscarAtualizarOuDeletar.getLogin(),
                "SenhaForte123!",
                "123456789" + String.format("%02d", counter),
                "55119876543" + String.format("%02d", counter),
                List.of(new EnderecoRequestDTO(
                        "SP", "São Paulo", "Centro", "Rua Teste " , 100 + counter, "Apto " + counter, "01000-" + String.format("%03d", counter)
                ))
        );
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateLoginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /proprietarios/{id} deve buscar proprietário por ID com sucesso se autenticado")
    void getProprietarioById_shouldReturnSuccessfullyWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/proprietarios/" + proprietarioParaBuscarAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(proprietarioParaBuscarAtualizarOuDeletar.getId().toString()))
                .andExpect(jsonPath("$.nome").value(proprietarioParaBuscarAtualizarOuDeletar.getNome()))
                .andExpect(jsonPath("$.email").value(proprietarioParaBuscarAtualizarOuDeletar.getEmail()))
                .andExpect(jsonPath("$.statusConta").value(proprietarioParaBuscarAtualizarOuDeletar.getStatusConta().toString()));
    }

    @Test
    @DisplayName("GET /proprietarios/{id} deve buscar proprietario por ID com sucesso se autenticado como o próprio proprietario")
    void getProprietarioById_shouldReturnProprietarioSuccessfullyWhenAuthenticatedAsSelf() throws Exception {
        mockMvc.perform(get("/proprietarios/" + proprietarioParaBuscarAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(proprietarioParaBuscarAtualizarOuDeletar.getId().toString()))
                .andExpect(jsonPath("$.nome").value(proprietarioParaBuscarAtualizarOuDeletar.getNome()));
    }

    @Test
    @DisplayName("DELETE /proprietarios/{id} deve deletar proprietario com sucesso se autenticado como o próprio proprietario")
    void deleteProprietarioById_shouldDeleteProprietarioSuccessfully() throws Exception {
        mockMvc.perform(delete("/proprietarios/" + proprietarioParaBuscarAtualizarOuDeletar.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Proprietário com ID " + proprietarioParaBuscarAtualizarOuDeletar.getId() + " excluído com sucesso."));

        assertFalse(proprietarioGateway.buscarProprietarioPorId(proprietarioParaBuscarAtualizarOuDeletar.getId()).isPresent());
    }
}