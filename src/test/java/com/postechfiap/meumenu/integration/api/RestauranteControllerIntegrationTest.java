package com.postechfiap.meumenu.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.meumenu.core.domain.entities.*;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.RestauranteGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.api.dtos.request.*;
import com.postechfiap.meumenu.infrastructure.api.dtos.response.LoginResponseDTO;
import com.postechfiap.meumenu.infrastructure.data.repositories.ProprietarioSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class RestauranteControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProprietarioGateway proprietarioGateway;

    @Autowired
    private UsuarioGateway usuarioGateway;

    @Autowired
    private RestauranteGateway restauranteGateway;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ProprietarioSpringRepository proprietarioSpringRepository;

    private CadastrarProprietarioRequestDTO cadastrarProprietarioRequestDTO;
    private CadastrarRestauranteRequestDTO request;
    private ProprietarioDomain proprietarioParaBuscarAtualizarOuDeletar;
    private RestauranteDomain restauranteParaBuscarAtualizarDeletar;
    private RestauranteDomain tempRestaurante;
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
                "SP",
                "São Paulo",
                "Centro",
                "Rua Teste " + uniqueSuffix,
                100 + counter,
                "Apto " + counter,
                "01000-" + String.format("%03d", counter)
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

        String loginExistente = "login_existente_" + uniqueSuffix;
        UUID proprietarioIdExistente = UUID.randomUUID();

        ProprietarioDomain tempProprietarioDomain = new ProprietarioDomain(
                proprietarioIdExistente,
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

        LoginRequestDTO loginRequest = new LoginRequestDTO(
                proprietarioParaBuscarAtualizarOuDeletar.getLogin(), rawPassword);
        String loginResponseContent = mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LoginResponseDTO loginResponse = objectMapper.readValue(loginResponseContent, LoginResponseDTO.class);
        jwtToken = loginResponse.token();

        request = new CadastrarRestauranteRequestDTO(
                "32112312311333",
                "Restaurante Teste ",
                "Restaurante da Esquina",
                "11999999999",
                "2983012803",
                new EnderecoRestauranteRequestDTO("SP", "Santo André", "Centro", "Rua da Paz", 123, "apto 101", "01000-000"),
                List.of(
                        new TipoCozinhaRequestDTO("Italiana")
                ),
                List.of(
                        new HorarioFuncionamentoRequestDTO(
                                LocalTime.of(11, 0, 0, 0),
                                LocalTime.of(22, 0, 0, 0),
                                DiaSemanaEnum.SEGUNDA_FEIRA)
                )
        );

        tempRestaurante = new RestauranteDomain(
                UUID.randomUUID(),
                "32112312311233",
                request.razaoSocial(),
                request.nomeFantasia(),
                request.inscricaoEstadual(),
                request.telefoneComercial(),
                proprietarioParaBuscarAtualizarOuDeletar,
                new EnderecoRestauranteDomain(
                        request.endereco().estado(),
                        request.endereco().cidade(),
                        request.endereco().bairro(),
                        request.endereco().rua(),
                        request.endereco().numero(),
                        request.endereco().complemento(),
                        request.endereco().cep()
                        ),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

    }

    @Test
    @DisplayName("POST /restaurantes deve cadastrar restaurante com sucesso se autenticado como Proprietário")
    void postRestaurante_deveCadastrarComSucesso() throws Exception {
        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeFantasia").value("Restaurante da Esquina"));
    }

    @Test
    @DisplayName("GET /restaurantes deve buscar restaurantes com sucesso")
    void getRestaurante_deveBuscarComSucesso() throws Exception {

        request = new CadastrarRestauranteRequestDTO(
                "12312312311333",
                "Restaurante Teste ",
                "Restaurante da Esquina",
                "11999999999",
                "2983012803",
                new EnderecoRestauranteRequestDTO("SP", "Santo André", "Centro", "Rua da Paz", 123, "apto 101", "01000-000"),
                List.of(
                        new TipoCozinhaRequestDTO("Italiana")
                ),
                List.of(
                        new HorarioFuncionamentoRequestDTO(
                                LocalTime.of(11, 0, 0, 0),
                                LocalTime.of(22, 0, 0, 0),
                                DiaSemanaEnum.SEGUNDA_FEIRA)
                )
        );

        mockMvc.perform(post("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + jwtToken));

        mockMvc.perform(get("/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].nomeFantasia").value("Restaurante da Esquina"));
    }
}
