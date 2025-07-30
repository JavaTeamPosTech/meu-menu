package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ClienteDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.AlergiaAlimentarEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.GeneroEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.MetodoPagamentoEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.TiposComidaEnum;
import com.postechfiap.meumenu.infrastructure.model.ClienteEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoEntity;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDataMapperImplTest {

    @InjectMocks
    private ClienteDataMapperImpl clienteDataMapper;

    private UUID clienteId;
    private UUID enderecoId;
    private ClienteDomain clienteDomain;
    private ClienteEntity clienteEntity;
    private EnderecoDomain enderecoDomain;
    private EnderecoEntity enderecoEntity;
    private UsuarioDomain usuarioDomainParaEndereco;
    private UsuarioEntity usuarioEntityParaEndereco;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteId = UUID.randomUUID();
        enderecoId = UUID.randomUUID();

        usuarioDomainParaEndereco = new UsuarioDomain(
                UUID.randomUUID(), "Nome Usuario End", "user.end@email.com", "userend", "hashpass",
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

        // 4. Configuração de ClienteDomain
        clienteDomain = new ClienteDomain(
                clienteId, "12345678900", LocalDate.of(1990, 1, 1), GeneroEnum.MASCULINO, "11987654321",
                new HashSet<>(Collections.singletonList(TiposComidaEnum.ITALIANA)),
                new HashSet<>(Collections.singletonList(AlergiaAlimentarEnum.GLUTEN)),
                MetodoPagamentoEnum.PIX, true, false, 0, 0, null,
                "Nome Cliente", "cliente@email.com", "clienteLogin", "senha_hash",
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5), Collections.singletonList(enderecoDomain)
        );
        // Garante que o EnderecoDomain tem a referência bidirecional (que seria setada no UseCase)
        enderecoDomain.setUsuario(clienteDomain);


        // 5. Configuração de ClienteEntity (simula um objeto vindo do banco)
        clienteEntity = new ClienteEntity(); // Usar setters para preencher
        clienteEntity.setId(clienteDomain.getId());
        clienteEntity.setNome(clienteDomain.getNome());
        clienteEntity.setEmail(clienteDomain.getEmail());
        clienteEntity.setLogin(clienteDomain.getLogin());
        clienteEntity.setSenha(clienteDomain.getSenha());
        clienteEntity.setDataCriacao(clienteDomain.getDataCriacao());
        clienteEntity.setDataAtualizacao(clienteDomain.getDataAtualizacao());
        clienteEntity.setCpf(clienteDomain.getCpf());
        clienteEntity.setDataNascimento(clienteDomain.getDataNascimento());
        clienteEntity.setGenero(clienteDomain.getGenero());
        clienteEntity.setTelefone(clienteDomain.getTelefone());
        clienteEntity.setPreferenciasAlimentares(new HashSet<>(clienteDomain.getPreferenciasAlimentares()));
        clienteEntity.setAlergias(new HashSet<>(clienteDomain.getAlergias()));
        clienteEntity.setMetodoPagamentoPreferido(clienteDomain.getMetodoPagamentoPreferido());
        clienteEntity.setUltimoPedido(clienteDomain.getUltimoPedido());
        clienteEntity.setSaldoPontos(clienteDomain.getSaldoPontos());
        clienteEntity.setClienteVip(clienteDomain.getClienteVip());
        clienteEntity.setAvaliacoesFeitas(clienteDomain.getAvaliacoesFeitas());
        clienteEntity.setNotificacoesAtivas(clienteDomain.getNotificacoesAtivas());
        clienteEntity.setEnderecos(Collections.singletonList(enderecoEntity));
        // Garante que o EnderecoEntity tem a referência bidirecional
        enderecoEntity.setUsuario(clienteEntity);
    }

    @Test
    @DisplayName("Deve mapear ClienteDomain para ClienteEntity corretamente")
    void toEntity_shouldMapClienteDomainToClienteEntityCorrectly() {
        ClienteEntity mappedEntity = clienteDataMapper.toEntity(clienteDomain);

        assertNotNull(mappedEntity);
        assertEquals(clienteDomain.getId(), mappedEntity.getId());
        assertEquals(clienteDomain.getNome(), mappedEntity.getNome());
        assertEquals(clienteDomain.getEmail(), mappedEntity.getEmail());
        assertEquals(clienteDomain.getLogin(), mappedEntity.getLogin());
        assertEquals(clienteDomain.getSenha(), mappedEntity.getSenha());
        assertEquals(clienteDomain.getDataCriacao(), mappedEntity.getDataCriacao());
        assertEquals(clienteDomain.getDataAtualizacao(), mappedEntity.getDataAtualizacao());
        assertEquals(clienteDomain.getCpf(), mappedEntity.getCpf());
        assertEquals(clienteDomain.getDataNascimento(), mappedEntity.getDataNascimento());
        assertEquals(clienteDomain.getGenero(), mappedEntity.getGenero());
        assertEquals(clienteDomain.getTelefone(), mappedEntity.getTelefone());
        assertEquals(clienteDomain.getPreferenciasAlimentares(), mappedEntity.getPreferenciasAlimentares());
        assertEquals(clienteDomain.getAlergias(), mappedEntity.getAlergias());
        assertEquals(clienteDomain.getMetodoPagamentoPreferido(), mappedEntity.getMetodoPagamentoPreferido());
        assertEquals(clienteDomain.getUltimoPedido(), mappedEntity.getUltimoPedido());
        assertEquals(clienteDomain.getSaldoPontos(), mappedEntity.getSaldoPontos());
        assertEquals(clienteDomain.getClienteVip(), mappedEntity.getClienteVip());
        assertEquals(clienteDomain.getAvaliacoesFeitas(), mappedEntity.getAvaliacoesFeitas());
        assertEquals(clienteDomain.getNotificacoesAtivas(), mappedEntity.getNotificacoesAtivas());

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
    @DisplayName("Deve mapear ClienteEntity para ClienteDomain corretamente")
    void toDomain_shouldMapClienteEntityToClienteDomainCorrectly() {
        ClienteDomain mappedDomain = clienteDataMapper.toDomain(clienteEntity);

        assertNotNull(mappedDomain);
        assertEquals(clienteEntity.getId(), mappedDomain.getId());
        assertEquals(clienteEntity.getNome(), mappedDomain.getNome());
        assertEquals(clienteEntity.getEmail(), mappedDomain.getEmail());
        assertEquals(clienteEntity.getLogin(), mappedDomain.getLogin());
        assertEquals(clienteEntity.getSenha(), mappedDomain.getSenha());
        assertEquals(clienteEntity.getDataCriacao(), mappedDomain.getDataCriacao());
        assertEquals(clienteEntity.getDataAtualizacao(), mappedDomain.getDataAtualizacao());
        assertEquals(clienteEntity.getCpf(), mappedDomain.getCpf());
        assertEquals(clienteEntity.getDataNascimento(), mappedDomain.getDataNascimento());
        assertEquals(clienteEntity.getGenero(), mappedDomain.getGenero());
        assertEquals(clienteEntity.getTelefone(), mappedDomain.getTelefone());
        assertEquals(clienteEntity.getPreferenciasAlimentares(), mappedDomain.getPreferenciasAlimentares());
        assertEquals(clienteEntity.getAlergias(), mappedDomain.getAlergias());
        assertEquals(clienteEntity.getMetodoPagamentoPreferido(), mappedDomain.getMetodoPagamentoPreferido());
        assertEquals(clienteEntity.getUltimoPedido(), mappedDomain.getUltimoPedido());
        assertEquals(clienteEntity.getSaldoPontos(), mappedDomain.getSaldoPontos());
        assertEquals(clienteEntity.getClienteVip(), mappedDomain.getClienteVip());
        assertEquals(clienteEntity.getAvaliacoesFeitas(), mappedDomain.getAvaliacoesFeitas());
        assertEquals(clienteEntity.getNotificacoesAtivas(), mappedDomain.getNotificacoesAtivas());

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
        EnderecoEntity mappedEntity = clienteDataMapper.toEnderecoEntity(enderecoDomain);

        assertNotNull(mappedEntity);
        assertEquals(enderecoDomain.getId(), mappedEntity.getId());
        assertEquals(enderecoDomain.getEstado(), mappedEntity.getEstado());
        assertEquals(enderecoDomain.getCidade(), mappedEntity.getCidade());
        assertEquals(enderecoDomain.getBairro(), mappedEntity.getBairro());
        assertEquals(enderecoDomain.getRua(), mappedEntity.getRua());
        assertEquals(enderecoDomain.getNumero(), mappedEntity.getNumero());
        assertEquals(enderecoDomain.getComplemento(), mappedEntity.getComplemento());
        assertEquals(enderecoDomain.getCep(), mappedEntity.getCep());
        // 'usuario' não é mapeado aqui, é setado no toEntity principal
        assertNull(mappedEntity.getUsuario());
    }

    @Test
    @DisplayName("Deve mapear EnderecoEntity para EnderecoDomain corretamente")
    void toEnderecoDomain_shouldMapCorrectly() {
        EnderecoDomain mappedDomain = clienteDataMapper.toEnderecoDomain(enderecoEntity);

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
    @DisplayName("Deve mapear lista de EnderecoDomain para lista de EnderecoEntity corretamente")
    void toEnderecoEntityList_shouldMapCorrectly() {
        List<EnderecoEntity> mappedList = clienteDataMapper.toEnderecoEntityList(Collections.singletonList(enderecoDomain));

        assertNotNull(mappedList);
        assertFalse(mappedList.isEmpty());
        assertEquals(1, mappedList.size());
        assertEquals(enderecoDomain.getId(), mappedList.get(0).getId());
    }

    @Test
    @DisplayName("Deve mapear lista de EnderecoEntity para lista de EnderecoDomain corretamente")
    void toEnderecoDomainList_shouldMapCorrectly() {
        List<EnderecoDomain> mappedList = clienteDataMapper.toEnderecoDomainList(Collections.singletonList(enderecoEntity));

        assertNotNull(mappedList);
        assertFalse(mappedList.isEmpty());
        assertEquals(1, mappedList.size());
        assertEquals(enderecoEntity.getId(), mappedList.get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar null quando ClienteDomain é null em toEntity")
    void toEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(clienteDataMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando ClienteEntity é null em toDomain")
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(clienteDataMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando EnderecoDomain é null em toEnderecoEntity")
    void toEnderecoEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(clienteDataMapper.toEnderecoEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando EnderecoEntity é null em toEnderecoDomain")
    void toEnderecoDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(clienteDataMapper.toEnderecoDomain(null));
    }

    @Test
    @DisplayName("Deve retornar null quando a lista de EnderecoDomain é null em toEnderecoEntityList")
    void toEnderecoEntityList_shouldReturnNullWhenDomainListIsNull() {
        assertNull(clienteDataMapper.toEnderecoEntityList(null));
    }

    @Test
    @DisplayName("Deve retornar null quando a lista de EnderecoEntity é null em toEnderecoDomainList")
    void toEnderecoDomainList_shouldReturnNullWhenEntityListIsNull() {
        assertNull(clienteDataMapper.toEnderecoDomainList(null));
    }

    @Test
    @DisplayName("Deve mapear sets vazios de preferencias e alergias corretamente para entity")
    void toEntity_shouldMapEmptySetsCorrectly() {
        clienteDomain = new ClienteDomain(
                clienteId, "123", LocalDate.now(), GeneroEnum.OUTRO, "tel",
                new HashSet<>(), new HashSet<>(), MetodoPagamentoEnum.DINHEIRO, false, false, 0, 0, null,
                "No Sets", "nosets@email.com", "nosets", "hash", LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );
        ClienteEntity mappedEntity = clienteDataMapper.toEntity(clienteDomain);
        assertNotNull(mappedEntity.getPreferenciasAlimentares());
        assertTrue(mappedEntity.getPreferenciasAlimentares().isEmpty());
        assertNotNull(mappedEntity.getAlergias());
        assertTrue(mappedEntity.getAlergias().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear sets vazios de preferencias e alergias corretamente para domain")
    void toDomain_shouldMapEmptySetsCorrectly() {
        clienteEntity.setPreferenciasAlimentares(new HashSet<>());
        clienteEntity.setAlergias(new HashSet<>());
        ClienteDomain mappedDomain = clienteDataMapper.toDomain(clienteEntity);
        assertNotNull(mappedDomain.getPreferenciasAlimentares());
        assertTrue(mappedDomain.getPreferenciasAlimentares().isEmpty());
        assertNotNull(mappedDomain.getAlergias());
        assertTrue(mappedDomain.getAlergias().isEmpty());
    }
}