package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDataMapperImplTest {

    @InjectMocks
    private UsuarioDataMapperImpl usuarioDataMapper;

    private UUID usuarioId;
    private UUID enderecoId;
    private UUID usuarioIdParaEndereco;
    private UsuarioDomain usuarioDomain;
    private UsuarioEntity usuarioEntity;
    private EnderecoDomain enderecoDomain;
    private EnderecoEntity enderecoEntity;
    private UsuarioDomain usuarioDomainParaEndereco;
    private UsuarioEntity usuarioEntityParaEndereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = UUID.randomUUID();
        enderecoId = UUID.randomUUID();
        usuarioIdParaEndereco = UUID.randomUUID();

        // 1. Configuração de UsuarioDomain e UsuarioEntity para o mapeamento de Endereço (dentro de EnderecoDomain)
        usuarioDomainParaEndereco = new UsuarioDomain(
                usuarioIdParaEndereco, "User Endereco", "user.end@email.com", "userend", "hashpass",
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(10), new ArrayList<>() // ArrayList para ser mutável
        );
        usuarioEntityParaEndereco = new UsuarioEntity();
        usuarioEntityParaEndereco.setId(usuarioDomainParaEndereco.getId());
        usuarioEntityParaEndereco.setNome(usuarioDomainParaEndereco.getNome());
        usuarioEntityParaEndereco.setEmail(usuarioDomainParaEndereco.getEmail());
        usuarioEntityParaEndereco.setLogin(usuarioDomainParaEndereco.getLogin());
        usuarioEntityParaEndereco.setSenha(usuarioDomainParaEndereco.getSenha());
        usuarioEntityParaEndereco.setDataCriacao(usuarioDomainParaEndereco.getDataCriacao());
        usuarioEntityParaEndereco.setDataAtualizacao(usuarioDomainParaEndereco.getDataAtualizacao());
        usuarioEntityParaEndereco.setEnderecos(new ArrayList<>());


        // 2. Configuração de EnderecoDomain (associado ao usuarioDomain)
        enderecoDomain = new EnderecoDomain(
                enderecoId,
                "SP", "Sao Paulo", "Bairro Legal", "Rua Principal", 100, "Apto 10", "01010-000",
                usuarioDomainParaEndereco // Associa o usuarioDomain
        );

        // 3. Configuração de UsuarioDomain principal para teste de toEntity
        usuarioDomain = new UsuarioDomain(
                usuarioId,
                "Usuario Principal",
                "principal@test.com",
                "principalLogin",
                "senha_hash_principal",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5),
                new ArrayList<>(Collections.singletonList(enderecoDomain)) // Lista de endereços
        );
        enderecoDomain.setUsuario(usuarioDomain); // Garante bidirecionalidade no Domain para o caso principal

        // 4. Configuração de EnderecoEntity (associado ao usuarioEntity)
        enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(enderecoId);
        enderecoEntity.setEstado("SP");
        enderecoEntity.setCidade("Sao Paulo");
        enderecoEntity.setBairro("Bairro Legal");
        enderecoEntity.setRua("Rua Principal");
        enderecoEntity.setNumero(100);
        enderecoEntity.setComplemento("Apto 10");
        enderecoEntity.setCep("01010000"); // Assumindo CEP sem hífen na Entity
        enderecoEntity.setUsuario(usuarioEntityParaEndereco); // Associa o usuarioEntity
        // usuarioEntity.setEnderecos(Collections.singletonList(enderecoEntity)); // Removido, será testado no toDomain


        // 5. Configuração de UsuarioEntity principal para teste de toDomain
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(usuarioId);
        usuarioEntity.setNome("Usuario Principal");
        usuarioEntity.setEmail("principal@test.com");
        usuarioEntity.setLogin("principalLogin");
        usuarioEntity.setSenha("senha_hash_principal");
        usuarioEntity.setDataCriacao(LocalDateTime.now().minusDays(10));
        usuarioEntity.setDataAtualizacao(LocalDateTime.now().minusDays(5));
        usuarioEntity.setEnderecos(new ArrayList<>(Collections.singletonList(enderecoEntity))); // Adiciona lista para toDomain
        enderecoEntity.setUsuario(usuarioEntity); // Garante bidirecionalidade na Entity para o caso principal
    }


    @Test
    @DisplayName("Deve mapear UsuarioDomain para UsuarioEntity corretamente (incluindo endereços)")
    void toEntity_shouldMapUsuarioDomainToUsuarioEntityCorrectly() {
        UsuarioEntity mappedEntity = usuarioDataMapper.toEntity(usuarioDomain);

        assertNotNull(mappedEntity);
        assertEquals(usuarioDomain.getId(), mappedEntity.getId());
        assertEquals(usuarioDomain.getNome(), mappedEntity.getNome());
        assertEquals(usuarioDomain.getEmail(), mappedEntity.getEmail());
        assertEquals(usuarioDomain.getLogin(), mappedEntity.getLogin());
        assertEquals(usuarioDomain.getSenha(), mappedEntity.getSenha());
        assertEquals(usuarioDomain.getDataCriacao(), mappedEntity.getDataCriacao());
        assertEquals(usuarioDomain.getDataAtualizacao(), mappedEntity.getDataAtualizacao());

        // Verifica endereços mapeados (via toEnderecoEntityList e mapEnderecoDomainToEntity indiretamente)
        assertNotNull(mappedEntity.getEnderecos());
        assertFalse(mappedEntity.getEnderecos().isEmpty());
        assertEquals(1, mappedEntity.getEnderecos().size());
        assertEquals(enderecoDomain.getId(), mappedEntity.getEnderecos().get(0).getId());
        // Verifica a associação inversa
        assertEquals(mappedEntity, mappedEntity.getEnderecos().get(0).getUsuario());
    }

    @Test
    @DisplayName("Deve mapear UsuarioEntity para UsuarioDomain corretamente (incluindo endereços)")
    void toDomain_shouldMapUsuarioEntityToUsuarioDomainCorrectly() {
        UsuarioDomain mappedDomain = usuarioDataMapper.toDomain(usuarioEntity);

        assertNotNull(mappedDomain);
        assertEquals(usuarioEntity.getId(), mappedDomain.getId());
        assertEquals(usuarioEntity.getNome(), mappedDomain.getNome());
        assertEquals(usuarioEntity.getEmail(), mappedDomain.getEmail());
        assertEquals(usuarioEntity.getLogin(), mappedDomain.getLogin());
        assertEquals(usuarioEntity.getSenha(), mappedDomain.getSenha());
        assertEquals(usuarioEntity.getDataCriacao(), mappedDomain.getDataCriacao());
        assertEquals(usuarioEntity.getDataAtualizacao(), mappedDomain.getDataAtualizacao());

        // Verifica endereços mapeados (via toEnderecoDomainList e mapEnderecoEntityToDomain indiretamente)
        assertNotNull(mappedDomain.getEnderecos());
        assertFalse(mappedDomain.getEnderecos().isEmpty());
        assertEquals(1, mappedDomain.getEnderecos().size());
        assertEquals(enderecoEntity.getId(), mappedDomain.getEnderecos().get(0).getId());
        assertEquals(enderecoEntity.getCep(), mappedDomain.getEnderecos().get(0).getCep());
        // Verifica o UsuarioDomain associado dentro do EnderecoDomain mapeado
        assertNotNull(mappedDomain.getEnderecos().get(0).getUsuario());
        assertNotSame(mappedDomain, mappedDomain.getEnderecos().get(0).getUsuario());
    }

    // --- Testes de null-safety e listas vazias/nulas ---

    @Test
    @DisplayName("Deve retornar null quando UsuarioDomain é null em toEntity")
    void toEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(usuarioDataMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando UsuarioEntity é null em toDomain")
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(usuarioDataMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve mapear listas vazias de endereços corretamente (toEntity)")
    void toEntity_shouldMapEmptyAddressListsCorrectly() {
        UsuarioDomain domainWithEmptyAddresses = new UsuarioDomain(
                UUID.randomUUID(), "Empty", "empty@email.com", "empty", "hash",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );
        UsuarioEntity mappedEntity = usuarioDataMapper.toEntity(domainWithEmptyAddresses);
        assertNotNull(mappedEntity.getEnderecos());
        assertTrue(mappedEntity.getEnderecos().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear listas vazias de endereços corretamente (toDomain)")
    void toDomain_shouldMapEmptyAddressListsCorrectly() {
        UsuarioEntity entityWithEmptyAddresses = new UsuarioEntity();
        entityWithEmptyAddresses.setId(UUID.randomUUID());
        entityWithEmptyAddresses.setEnderecos(Collections.emptyList());
        UsuarioDomain mappedDomain = usuarioDataMapper.toDomain(entityWithEmptyAddresses);
        assertNotNull(mappedDomain.getEnderecos());
        assertTrue(mappedDomain.getEnderecos().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear UsuarioDomain com endereços nulos para UsuarioEntity corretamente")
    void toEntity_shouldHandleNullAddressList() {
        UsuarioDomain domainWithNullAddresses = new UsuarioDomain(
                UUID.randomUUID(), "Null Addr", "null@email.com", "nulluser", "hash",
                LocalDateTime.now(), LocalDateTime.now(), null // Lista de endereços nula
        );
        UsuarioEntity mappedEntity = usuarioDataMapper.toEntity(domainWithNullAddresses);
        assertNotNull(mappedEntity);
    }

    @Test
    @DisplayName("Deve mapear UsuarioEntity com endereços nulos para UsuarioDomain corretamente")
    void toDomain_shouldHandleNullAddressList() {
        UsuarioEntity entityWithNullAddresses = new UsuarioEntity();
        entityWithNullAddresses.setId(UUID.randomUUID());
        entityWithNullAddresses.setEnderecos(null); // Lista de endereços nula
        UsuarioDomain mappedDomain = usuarioDataMapper.toDomain(entityWithNullAddresses);
        assertNotNull(mappedDomain);
    }
}