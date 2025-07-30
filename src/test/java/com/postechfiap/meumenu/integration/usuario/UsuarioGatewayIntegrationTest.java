package com.postechfiap.meumenu.integration.usuario;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.infrastructure.data.repositories.UsuarioSpringRepository;
import com.postechfiap.meumenu.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class UsuarioGatewayIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UsuarioGateway usuarioGateway;
    @Autowired
    private UsuarioSpringRepository usuarioSpringRepository;

    private UsuarioDomain usuarioDomain;
    private static int counter = 0;

    @BeforeEach
    void setUp() {
        counter++;

        usuarioDomain = new UsuarioDomain(
                UUID.randomUUID(),
                "Usuario Teste " + counter,
                "usuario" + counter + "@test.com",
                "login_user" + counter,
                "senha_hash_" + counter,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5),
                Collections.emptyList()
        );
    }

    @Autowired
    private com.postechfiap.meumenu.core.gateways.ClienteGateway clienteGateway;

    private UsuarioDomain persistirUsuarioParaTeste(UsuarioDomain domain) {

        com.postechfiap.meumenu.core.domain.entities.ClienteDomain clienteParaTeste = new com.postechfiap.meumenu.core.domain.entities.ClienteDomain(
                domain.getNome(),
                domain.getEmail(),
                domain.getLogin(),
                domain.getSenha(),
                "111222333" + String.format("%02d", counter), // CPF para Cliente
                LocalDate.now().minusYears(20),
                com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum.OUTRO,
                "2199999" + String.format("%02d", counter),
                Collections.emptySet(),
                Collections.emptySet(),
                com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum.PIX,
                true,
                false
        );

        clienteParaTeste.setId(domain.getId()); // Setar o ID do usuarioDomain original para que seja o mesmo
        clienteParaTeste.setDataCriacao(domain.getDataCriacao());
        clienteParaTeste.setDataAtualizacao(domain.getDataAtualizacao());

        return clienteGateway.cadastrarCliente(clienteParaTeste); // Retorna ClienteDomain, que é um UsuarioDomain
    }


    // --- Testes de EXISTÊNCIA ---
    @Test
    @DisplayName("Deve verificar que o login existe no banco de dados para Usuário")
    void shouldExistsByLoginInDatabase() {
        UsuarioDomain savedUser = persistirUsuarioParaTeste(usuarioDomain);
        assertTrue(usuarioGateway.existsByLogin(savedUser.getLogin()));
    }

    @Test
    @DisplayName("Deve verificar que o login não existe no banco de dados para Usuário")
    void shouldNotExistsByLoginInDatabase() {
        assertFalse(usuarioGateway.existsByLogin("nonexistent_login_xyz"));
    }

    @Test
    @DisplayName("Deve verificar que o email existe no banco de dados para Usuário")
    void shouldExistsByEmailInDatabase() {
        UsuarioDomain savedUser = persistirUsuarioParaTeste(usuarioDomain);
        assertTrue(usuarioGateway.existsByEmail(savedUser.getEmail()));
    }

    @Test
    @DisplayName("Deve verificar que o email não existe no banco de dados para Usuário")
    void shouldNotExistsByEmailInDatabase() {
        assertFalse(usuarioGateway.existsByEmail("nonexistent_email_xyz@domain.com"));
    }

    // --- Testes de BUSCA POR ID ---
    @Test
    @DisplayName("Deve buscar um usuário por ID no banco de dados")
    void shouldBuscarUsuarioByIdInDatabase() {
        UsuarioDomain savedUser = persistirUsuarioParaTeste(usuarioDomain);

        Optional<UsuarioDomain> foundUserOptional = usuarioGateway.buscarUsuarioPorId(savedUser.getId());

        assertTrue(foundUserOptional.isPresent());
        UsuarioDomain foundUser = foundUserOptional.get();
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getLogin(), foundUser.getLogin());
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar usuário por ID inexistente")
    void shouldReturnEmptyWhenBuscarUsuarioByIdDoesNotExist() {
        Optional<UsuarioDomain> foundUserOptional = usuarioGateway.buscarUsuarioPorId(UUID.randomUUID());
        assertTrue(foundUserOptional.isEmpty());
    }

    // --- Testes de ATUALIZAÇÃO ---
    @Test
    @DisplayName("Deve atualizar os dados de um usuário no banco de dados")
    void shouldAtualizarUsuarioInDatabase() {
        UsuarioDomain savedUser = persistirUsuarioParaTeste(usuarioDomain);

        // Preparar dados para atualização
        String newName = "Usuario Atualizado";
        String newEmail = "updated_user@test.com";
        String newLogin = "updated_login";
        String newPasswordHash = "new_password_hash";

        // Atualiza o UsuarioDomain (simula o que o UseCase faria)
        savedUser.setNome(newName);
        savedUser.setEmail(newEmail);
        savedUser.setLogin(newLogin);
        savedUser.setSenha(newPasswordHash);
        savedUser.setDataAtualizacao(LocalDateTime.now());

        // Ação: Atualizar o usuário via Gateway
        UsuarioDomain updatedUser = usuarioGateway.atualizarUsuario(savedUser);

        // Verificações
        assertNotNull(updatedUser);
        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals(newName, updatedUser.getNome());
        assertEquals(newEmail, updatedUser.getEmail());
        assertEquals(newLogin, updatedUser.getLogin());
        assertEquals(newPasswordHash, updatedUser.getSenha());

        // Verificar se os dados no banco foram realmente atualizados
        Optional<UsuarioDomain> foundUpdatedUserOptional = usuarioGateway.buscarUsuarioPorId(savedUser.getId());
        assertTrue(foundUpdatedUserOptional.isPresent());
        UsuarioDomain foundUpdatedUser = foundUpdatedUserOptional.get();
        assertEquals(newName, foundUpdatedUser.getNome());
        assertEquals(newEmail, foundUpdatedUser.getEmail());
        assertEquals(newLogin, foundUpdatedUser.getLogin());
        assertEquals(newPasswordHash, foundUpdatedUser.getSenha());
    }
}