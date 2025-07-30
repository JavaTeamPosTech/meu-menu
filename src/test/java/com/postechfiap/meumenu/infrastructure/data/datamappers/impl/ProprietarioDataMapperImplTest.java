package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProprietarioDataMapperImplTest {

    @InjectMocks
    private ProprietarioDataMapperImpl proprietarioDataMapper;

    private UUID proprietarioId;
    private UUID enderecoId;
    private UUID usuarioIdParaEndereco;
    private ProprietarioDomain proprietarioDomain;
    private ProprietarioEntity proprietarioEntity;
    private EnderecoDomain enderecoDomain;
    private EnderecoEntity enderecoEntity;
    private UsuarioDomain usuarioDomainParaEndereco;
    private UsuarioEntity usuarioEntityParaEndereco;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa @InjectMocks manualmente

        proprietarioId = UUID.randomUUID();
        enderecoId = UUID.randomUUID();
        usuarioIdParaEndereco = UUID.randomUUID();

        // 1. Configuração de UsuarioDomain e UsuarioEntity para o mapeamento de Endereço (dentro de EnderecoDomain)
        usuarioDomainParaEndereco = new UsuarioDomain(
                usuarioIdParaEndereco, "User Endereco", "user.end@email.com", "userend", "hashpass",
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(10), Collections.emptyList()
        );
        usuarioEntityParaEndereco = new UsuarioEntity(
                usuarioDomainParaEndereco.getId(), usuarioDomainParaEndereco.getNome(), usuarioDomainParaEndereco.getEmail(),
                usuarioDomainParaEndereco.getLogin(), usuarioDomainParaEndereco.getSenha(),
                usuarioDomainParaEndereco.getDataCriacao(), usuarioDomainParaEndereco.getDataAtualizacao(), Collections.emptyList()
        );

        // 2. Configuração de EnderecoDomain
        enderecoDomain = new EnderecoDomain(
                enderecoId, "SP", "Sao Paulo", "Bairro X", "Rua Y", 123, "Apto 1", "01000000",
                usuarioDomainParaEndereco
        );

        // 3. Configuração de EnderecoEntity
        enderecoEntity = new EnderecoEntity(
                enderecoId, "SP", "Sao Paulo", "Bairro X", "Rua Y", 123, "Apto 1", "01000000",
                usuarioEntityParaEndereco
        );

        // 4. Configuração de ProprietarioDomain
        proprietarioDomain = new ProprietarioDomain(
                proprietarioId, "12345678900", "11987654321", StatusContaEnum.ATIVO,
                "Nome Proprietario", "prop@email.com", "login_prop", "senha_prop_hash",
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5),
                Collections.singletonList(enderecoDomain) // Lista de Endereços

        );
        enderecoDomain.setUsuario(proprietarioDomain); // Garante bidirecionalidade no Domain

        // 5. Configuração de ProprietarioEntity
        proprietarioEntity = new ProprietarioEntity(
                proprietarioId,
                "Nome Proprietario",
                "prop@email.com",
                "login_prop",
                "senha_prop_hash",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5),
                Collections.singletonList(enderecoEntity), // Lista de Endereços
                "12345678900",
                "11987654321",
                StatusContaEnum.ATIVO
        );

        enderecoEntity.setUsuario(proprietarioEntity); // Garante bidirecionalidade na Entity
    }

    @Test
    @DisplayName("Deve mapear ProprietarioDomain para ProprietarioEntity corretamente")
    void toEntity_shouldMapProprietarioDomainToProprietarioEntityCorrectly() {
        ProprietarioEntity mappedEntity = proprietarioDataMapper.toEntity(proprietarioDomain);

        assertNotNull(mappedEntity);
        assertEquals(proprietarioDomain.getId(), mappedEntity.getId());
        assertEquals(proprietarioDomain.getCpf(), mappedEntity.getCpf());
        assertEquals(proprietarioDomain.getWhatsapp(), mappedEntity.getWhatsapp());
        assertEquals(proprietarioDomain.getStatusConta(), mappedEntity.getStatusConta());
        assertEquals(proprietarioDomain.getNome(), mappedEntity.getNome());
        assertEquals(proprietarioDomain.getEmail(), mappedEntity.getEmail());
        assertEquals(proprietarioDomain.getLogin(), mappedEntity.getLogin());
        assertEquals(proprietarioDomain.getSenha(), mappedEntity.getSenha());
        assertEquals(proprietarioDomain.getDataCriacao(), mappedEntity.getDataCriacao());
        assertEquals(proprietarioDomain.getDataAtualizacao(), mappedEntity.getDataAtualizacao());

        // Verifica endereços
        assertNotNull(mappedEntity.getEnderecos());
        assertFalse(mappedEntity.getEnderecos().isEmpty());
        assertEquals(1, mappedEntity.getEnderecos().size());
        assertEquals(enderecoEntity.getId(), mappedEntity.getEnderecos().get(0).getId());
        assertEquals(enderecoEntity.getCep(), mappedEntity.getEnderecos().get(0).getCep());
        // Verifica a associação inversa
        assertEquals(mappedEntity, mappedEntity.getEnderecos().get(0).getUsuario());
    }

    @Test
    @DisplayName("Deve mapear ProprietarioEntity para ProprietarioDomain corretamente")
    void toDomain_shouldMapProprietarioEntityToProprietarioDomainCorrectly() {
        ProprietarioDomain mappedDomain = proprietarioDataMapper.toDomain(proprietarioEntity);

        assertNotNull(mappedDomain);
        assertEquals(proprietarioEntity.getId(), mappedDomain.getId());
        assertEquals(proprietarioEntity.getCpf(), mappedDomain.getCpf());
        assertEquals(proprietarioEntity.getWhatsapp(), mappedDomain.getWhatsapp());
        assertEquals(proprietarioEntity.getStatusConta(), mappedDomain.getStatusConta());
        assertEquals(proprietarioEntity.getNome(), mappedDomain.getNome());
        assertEquals(proprietarioEntity.getEmail(), mappedDomain.getEmail());
        assertEquals(proprietarioEntity.getLogin(), mappedDomain.getLogin());
        assertEquals(proprietarioEntity.getSenha(), mappedDomain.getSenha());
        assertEquals(proprietarioEntity.getDataCriacao(), mappedDomain.getDataCriacao());
        assertEquals(proprietarioEntity.getDataAtualizacao(), mappedDomain.getDataAtualizacao());

        // Verifica endereços
        assertNotNull(mappedDomain.getEnderecos());
        assertFalse(mappedDomain.getEnderecos().isEmpty());
        assertEquals(1, mappedDomain.getEnderecos().size());
        assertEquals(enderecoDomain.getId(), mappedDomain.getEnderecos().get(0).getId());
        assertEquals(enderecoDomain.getCep(), mappedDomain.getEnderecos().get(0).getCep());
        // Verifica a associação inversa (UsuarioDomain do EnderecoDomain)
        assertNotNull(mappedDomain.getEnderecos().get(0).getUsuario());
    }

    @Test
    @DisplayName("Deve mapear EnderecoDomain para EnderecoEntity corretamente")
    void toEnderecoEntity_shouldMapCorrectly() {
        EnderecoEntity mappedEntity = proprietarioDataMapper.toEnderecoEntity(enderecoDomain);

        assertNotNull(mappedEntity);
        assertEquals(enderecoDomain.getId(), mappedEntity.getId());
        assertEquals(enderecoDomain.getEstado(), mappedEntity.getEstado());
        assertEquals(enderecoDomain.getCidade(), mappedEntity.getCidade());
        assertEquals(enderecoDomain.getBairro(), mappedEntity.getBairro());
        assertEquals(enderecoDomain.getRua(), mappedEntity.getRua());
        assertEquals(enderecoDomain.getNumero(), mappedEntity.getNumero());
        assertEquals(enderecoDomain.getComplemento(), mappedEntity.getComplemento());
        assertEquals(enderecoDomain.getCep(), mappedEntity.getCep());
        assertNull(mappedEntity.getUsuario()); // 'usuario' é setado no toEntity principal
    }

    @Test
    @DisplayName("Deve mapear EnderecoEntity para EnderecoDomain corretamente")
    void toEnderecoDomain_shouldMapCorrectly() {
        EnderecoDomain mappedDomain = proprietarioDataMapper.toEnderecoDomain(enderecoEntity);

        assertNotNull(mappedDomain);
        assertEquals(enderecoEntity.getId(), mappedDomain.getId());
        assertEquals(enderecoEntity.getEstado(), mappedDomain.getEstado());
        assertEquals(enderecoEntity.getCidade(), mappedDomain.getCidade());
        assertEquals(enderecoEntity.getBairro(), mappedDomain.getBairro());
        assertEquals(enderecoEntity.getRua(), mappedDomain.getRua());
        assertEquals(enderecoEntity.getNumero(), mappedDomain.getNumero());
        assertEquals(enderecoEntity.getComplemento(), mappedDomain.getComplemento());
        assertEquals(enderecoEntity.getCep(), mappedDomain.getCep());
        // Verifica o UsuarioDomain associado
        assertNotNull(mappedDomain.getUsuario());
    }

    @Test
    @DisplayName("Deve retornar null quando ProprietarioDomain é null em toEntity")
    void toEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(proprietarioDataMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando ProprietarioEntity é null em toDomain")
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(proprietarioDataMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando EnderecoDomain é null em toEnderecoEntity")
    void toEnderecoEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(proprietarioDataMapper.toEnderecoEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando EnderecoEntity é null em toEnderecoDomain")
    void toEnderecoDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(proprietarioDataMapper.toEnderecoDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando a lista de EnderecoDomain é null em toEnderecoEntityList")
    void toEnderecoEntityList_shouldReturnNullWhenDomainListIsNull() {
        assertNull(proprietarioDataMapper.toEnderecoEntityList(null));
    }

    @Test
    @DisplayName("Deve retornar null quando a lista de EnderecoEntity é null em toEnderecoDomainList")
    void toEnderecoDomainList_shouldReturnNullWhenEntityListIsNull() {
        assertNull(proprietarioDataMapper.toEnderecoDomainList(null));
    }

    @Test
    @DisplayName("Deve mapear listas vazias de Enderecos corretamente")
    void shouldMapEmptyAddressListsCorrectly() {
        proprietarioDomain = new ProprietarioDomain(
                proprietarioId, "cpf", "zap", StatusContaEnum.ATIVO,
                "nome", "email", "login", "senha",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );
        ProprietarioEntity mappedEntity = proprietarioDataMapper.toEntity(proprietarioDomain);
        assertNotNull(mappedEntity.getEnderecos());
        assertTrue(mappedEntity.getEnderecos().isEmpty());

        proprietarioEntity.setEnderecos(Collections.emptyList());
        ProprietarioDomain mappedDomain = proprietarioDataMapper.toDomain(proprietarioEntity);
        assertNotNull(mappedDomain.getEnderecos());
        assertTrue(mappedDomain.getEnderecos().isEmpty());
    }
}