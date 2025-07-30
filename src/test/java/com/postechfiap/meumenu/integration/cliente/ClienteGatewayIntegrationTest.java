package com.postechfiap.meumenu.integration.cliente;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.core.gateways.ClienteGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.data.repositories.ClienteSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
class ClienteGatewayIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ClienteGateway clienteGateway;

    @Autowired
    private UsuarioGateway usuarioGateway;

    @Autowired
    private ClienteSpringRepository clienteSpringRepository;

    private ClienteDomain clienteDomain;
    private EnderecoDomain enderecoDomain;

    @BeforeEach
    void setUp() {
        enderecoDomain = new EnderecoDomain(
                UUID.randomUUID(), "SP", "Sao Paulo", "Centro", "Rua Teste", 100, "Apto 1", "01000000",
                null
        );

        clienteDomain = new ClienteDomain(
                UUID.randomUUID(), // ID
                "12345678900", // CPF original
                LocalDate.of(1985, 10, 20),
                GeneroEnum.MASCULINO,
                "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX,
                true, false, 0, 0, null,
                "Teste Cliente", "cliente@email.com", "testelogin", "senha_hash",
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10), Collections.emptyList()
        );

        clienteDomain.setEnderecos(Collections.singletonList(enderecoDomain));
        enderecoDomain.setUsuario(clienteDomain); // EnderecoDomain precisa de um UsuarioDomain associado
    }

    @Test
    @DisplayName("Deve cadastrar um novo cliente no banco de dados")
    void shouldCadastrarClienteInDatabase() {
        // Garantir que não existe antes
        assertFalse(clienteGateway.existsByCpf(clienteDomain.getCpf()));
        assertFalse(usuarioGateway.existsByLogin(clienteDomain.getLogin()));
        assertFalse(usuarioGateway.existsByEmail(clienteDomain.getEmail()));

        ClienteDomain savedCliente = clienteGateway.cadastrarCliente(clienteDomain);

        assertNotNull(savedCliente);
        assertNotNull(savedCliente.getId());
        assertEquals(clienteDomain.getCpf(), savedCliente.getCpf());

        assertTrue(clienteGateway.buscarClientePorId(savedCliente.getId()).isPresent());
        assertTrue(clienteGateway.existsByCpf(clienteDomain.getCpf()));
        assertTrue(usuarioGateway.existsByLogin(clienteDomain.getLogin()));
        assertTrue(usuarioGateway.existsByEmail(clienteDomain.getEmail()));

        assertFalse(savedCliente.getEnderecos().isEmpty());
        EnderecoDomain savedEndereco = savedCliente.getEnderecos().get(0);
        assertNotNull(savedEndereco.getId()); // Endereço deve ter ID
        assertEquals(enderecoDomain.getCep(), savedEndereco.getCep());
        assertNotNull(savedEndereco.getUsuario());
        assertEquals(savedCliente.getId(), savedEndereco.getUsuario().getId());
    }

    @Test
    @DisplayName("Deve buscar um cliente por ID no banco de dados")
    void shouldBuscarClienteByIdInDatabase() {
        ClienteDomain savedCliente = clienteGateway.cadastrarCliente(clienteDomain);
        assertNotNull(savedCliente.getId());

        Optional<ClienteDomain> foundClienteOptional = clienteGateway.buscarClientePorId(savedCliente.getId());

        assertTrue(foundClienteOptional.isPresent());
        ClienteDomain foundCliente = foundClienteOptional.get();
        assertEquals(savedCliente.getId(), foundCliente.getId());
        assertEquals(savedCliente.getCpf(), foundCliente.getCpf());
        assertEquals(savedCliente.getNome(), foundCliente.getNome());

        assertFalse(foundCliente.getEnderecos().isEmpty());
        assertEquals(savedCliente.getEnderecos().get(0).getId(), foundCliente.getEnderecos().get(0).getId());
        assertEquals(savedCliente.getEnderecos().get(0).getCep(), foundCliente.getEnderecos().get(0).getCep());
        assertNotNull(foundCliente.getEnderecos().get(0).getUsuario());
        assertEquals(foundCliente.getId(), foundCliente.getEnderecos().get(0).getUsuario().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente por ID inexistente")
    void shouldReturnEmptyWhenBuscarClienteByIdDoesNotExist() {
        Optional<ClienteDomain> foundClienteOptional = clienteGateway.buscarClientePorId(UUID.randomUUID());

        assertTrue(foundClienteOptional.isEmpty());
    }

    @Test
    @DisplayName("Deve verificar que o CPF existe no banco de dados")
    void shouldExistsByCpfInDatabase() {
        clienteGateway.cadastrarCliente(clienteDomain);
        assertTrue(clienteGateway.existsByCpf(clienteDomain.getCpf()));
    }

    @Test
    @DisplayName("Deve verificar que o CPF não existe no banco de dados")
    void shouldNotExistsByCpfInDatabase() {
        assertFalse(clienteGateway.existsByCpf("99999999999"));
    }

    @Test
    @DisplayName("Deve verificar que o login existe no banco de dados")
    void shouldExistsByLoginInDatabase() {
        clienteGateway.cadastrarCliente(clienteDomain);
        assertTrue(usuarioGateway.existsByLogin(clienteDomain.getLogin()));
    }

    @Test
    @DisplayName("Deve verificar que o login não existe no banco de dados")
    void shouldNotExistsByLoginInDatabase() {
        assertFalse(usuarioGateway.existsByLogin("nonexistentlogin"));
    }

    @Test
    @DisplayName("Deve verificar que o email existe no banco de dados")
    void shouldExistsByEmailInDatabase() {
        clienteGateway.cadastrarCliente(clienteDomain);
        assertTrue(usuarioGateway.existsByEmail(clienteDomain.getEmail()));
    }

    @Test
    @DisplayName("Deve verificar que o email não existe no banco de dados")
    void shouldNotExistsByEmailInDatabase() {
        assertFalse(usuarioGateway.existsByEmail("nonexistent@email.com"));
    }
}