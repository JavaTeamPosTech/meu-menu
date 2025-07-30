package com.postechfiap.meumenu.integration.cliente;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.data.repositories.ProprietarioSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ProprietarioGatewayIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProprietarioGateway proprietarioGateway;

    @Autowired
    private UsuarioGateway usuarioGateway; // Para existsByLogin, existsByEmail

    @Autowired
    private ProprietarioSpringRepository proprietarioSpringRepository;

    private ProprietarioDomain proprietarioDomain;
    private EnderecoDomain enderecoDomain;
    private static int counter = 0;

    @BeforeEach
    void setUp() {
        counter++;

        enderecoDomain = new EnderecoDomain(
                UUID.randomUUID(), "SP", "Sao Paulo", "Centro", "Rua Prop" + counter, 100 + counter, "Apto " + counter, "01000" + counter + "-000",
                null
        );

        proprietarioDomain = new ProprietarioDomain(
                UUID.randomUUID(), // ID inicial (será sobrescrito pelo JPA)
                "111222333" + String.format("%02d", counter), // CPF único
                "119876543" + String.format("%02d", counter), // Whatsapp único
                StatusContaEnum.ATIVO,
                "Proprietario Teste " + counter,
                "prop" + counter + "@email.com", // Email único
                "proplogin" + counter, // Login único
                "senha_hash_" + counter, // Senha criptografada
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5),
                Collections.singletonList(enderecoDomain) // Endereços
        );
        // Setar a relação bidirecional no domínio
        enderecoDomain.setUsuario(proprietarioDomain);
    }

    @Test
    @DisplayName("Deve cadastrar um novo proprietário no banco de dados")
    void shouldCadastrarProprietarioInDatabase() {
        // Pré-condições
        assertFalse(proprietarioGateway.existsByCpf(proprietarioDomain.getCpf()));
        assertFalse(usuarioGateway.existsByLogin(proprietarioDomain.getLogin()));
        assertFalse(usuarioGateway.existsByEmail(proprietarioDomain.getEmail()));

        // Ação
        ProprietarioDomain savedProprietario = proprietarioGateway.cadastrarProprietario(proprietarioDomain);

        // Verificações
        assertNotNull(savedProprietario);
        assertNotNull(savedProprietario.getId()); // ID deve ser gerado pelo banco
        assertEquals(proprietarioDomain.getCpf(), savedProprietario.getCpf());
        assertEquals(proprietarioDomain.getNome(), savedProprietario.getNome());

        // Verificar existência e dados no banco
        assertTrue(proprietarioGateway.buscarProprietarioPorId(savedProprietario.getId()).isPresent());
        assertTrue(proprietarioGateway.existsByCpf(proprietarioDomain.getCpf()));
        assertTrue(usuarioGateway.existsByLogin(proprietarioDomain.getLogin()));
        assertTrue(usuarioGateway.existsByEmail(proprietarioDomain.getEmail()));

        // Verificar endereço salvo em cascata
        assertFalse(savedProprietario.getEnderecos().isEmpty());
        EnderecoDomain savedEndereco = savedProprietario.getEnderecos().get(0);
        assertNotNull(savedEndereco.getId()); // Endereço deve ter ID
        assertEquals(enderecoDomain.getCep(), savedEndereco.getCep());
        assertNotNull(savedEndereco.getUsuario());
        assertEquals(savedProprietario.getId(), savedEndereco.getUsuario().getId());
    }

    @Test
    @DisplayName("Deve buscar um proprietário por ID no banco de dados")
    void shouldBuscarProprietarioByIdInDatabase() {
        // Pré-condição: Cadastrar um proprietário
        ProprietarioDomain savedProprietario = proprietarioGateway.cadastrarProprietario(proprietarioDomain);
        assertNotNull(savedProprietario.getId());

        // Ação: Buscar pelo ID
        Optional<ProprietarioDomain> foundProprietarioOptional = proprietarioGateway.buscarProprietarioPorId(savedProprietario.getId());

        // Verificações
        assertTrue(foundProprietarioOptional.isPresent());
        ProprietarioDomain foundProprietario = foundProprietarioOptional.get();
        assertEquals(savedProprietario.getId(), foundProprietario.getId());
        assertEquals(savedProprietario.getCpf(), foundProprietario.getCpf());
        assertEquals(savedProprietario.getNome(), foundProprietario.getNome());

        // Verificar endereço carregado
        assertFalse(foundProprietario.getEnderecos().isEmpty());
        assertEquals(savedProprietario.getEnderecos().get(0).getId(), foundProprietario.getEnderecos().get(0).getId());
        assertEquals(savedProprietario.getEnderecos().get(0).getCep(), foundProprietario.getEnderecos().get(0).getCep());
        assertNotNull(foundProprietario.getEnderecos().get(0).getUsuario());
        assertEquals(foundProprietario.getId(), foundProprietario.getEnderecos().get(0).getUsuario().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar proprietário por ID inexistente")
    void shouldReturnEmptyWhenBuscarProprietarioByIdDoesNotExist() {
        Optional<ProprietarioDomain> foundProprietarioOptional = proprietarioGateway.buscarProprietarioPorId(UUID.randomUUID());
        assertTrue(foundProprietarioOptional.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um proprietário do banco de dados")
    void shouldDeletarProprietarioInDatabase() {
        // Pré-condição: Cadastrar um proprietário
        ProprietarioDomain savedProprietario = proprietarioGateway.cadastrarProprietario(proprietarioDomain);
        assertNotNull(savedProprietario.getId());

        // Ação: Deletar pelo ID
        proprietarioGateway.deletarProprietario(savedProprietario.getId());

        // Verificação: Não deve mais existir
        assertFalse(proprietarioGateway.buscarProprietarioPorId(savedProprietario.getId()).isPresent());
        assertFalse(proprietarioGateway.existsByCpf(proprietarioDomain.getCpf()));
        assertFalse(usuarioGateway.existsByLogin(proprietarioDomain.getLogin()));
        assertFalse(usuarioGateway.existsByEmail(proprietarioDomain.getEmail()));
    }

    @Test
    @DisplayName("Deve atualizar os dados de um proprietário no banco de dados")
    void shouldAtualizarProprietarioInDatabase() {
        // Pré-condição: Cadastrar um proprietário
        ProprietarioDomain savedProprietario = proprietarioGateway.cadastrarProprietario(proprietarioDomain);
        assertNotNull(savedProprietario.getId());

        // Preparar dados para atualização
        ProprietarioDomain updatedProprietarioDomain = new ProprietarioDomain(
                savedProprietario.getId(), // ID do existente
                "22233344455", // Novo CPF
                "21998877665", // Novo Whatsapp
                StatusContaEnum.DESATIVADO, // Novo Status
                "Proprietario Atualizado", // Novo Nome
                "atualizado.prop@email.com", // Novo Email
                "login.updated.prop", // Novo Login
                savedProprietario.getSenha(), // Senha original
                savedProprietario.getDataCriacao(), // Data Criação original
                LocalDateTime.now(), // Data Atualização (simula que mudou)
                savedProprietario.getEnderecos() // Endereços (os mesmos)
        );
        // Ação: Atualizar o proprietário
        ProprietarioDomain resultProprietario = proprietarioGateway.atualizarProprietario(updatedProprietarioDomain);

        // Verificações
        assertNotNull(resultProprietario);
        assertEquals(savedProprietario.getId(), resultProprietario.getId());
        assertEquals("22233344455", resultProprietario.getCpf());
        assertEquals("21998877665", resultProprietario.getWhatsapp());
        assertEquals(StatusContaEnum.DESATIVADO, resultProprietario.getStatusConta());
        assertEquals("Proprietario Atualizado", resultProprietario.getNome());
        assertEquals("atualizado.prop@email.com", resultProprietario.getEmail());
        assertEquals("login.updated.prop", resultProprietario.getLogin());

        // Verificar se os dados no banco foram realmente atualizados
        Optional<ProprietarioDomain> foundUpdatedProprietarioOptional = proprietarioGateway.buscarProprietarioPorId(savedProprietario.getId());
        assertTrue(foundUpdatedProprietarioOptional.isPresent());
        ProprietarioDomain foundUpdatedProprietario = foundUpdatedProprietarioOptional.get();
        assertEquals("22233344455", foundUpdatedProprietario.getCpf());
        assertEquals("atualizado.prop@email.com", foundUpdatedProprietario.getEmail());
        assertEquals("login.updated.prop", foundUpdatedProprietario.getLogin());
        assertEquals(StatusContaEnum.DESATIVADO, foundUpdatedProprietario.getStatusConta());
    }

    @Test
    @DisplayName("Deve verificar que o CPF existe no banco de dados para Proprietário")
    void shouldExistsByCpfProprietarioInDatabase() {
        proprietarioGateway.cadastrarProprietario(proprietarioDomain);
        assertTrue(proprietarioGateway.existsByCpf(proprietarioDomain.getCpf()));
    }

    @Test
    @DisplayName("Deve verificar que o CPF não existe no banco de dados para Proprietário")
    void shouldNotExistsByCpfProprietarioInDatabase() {
        assertFalse(proprietarioGateway.existsByCpf("99999999999"));
    }
}