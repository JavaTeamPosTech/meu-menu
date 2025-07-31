package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.postechfiap.meumenu.core.domain.entities.RestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.entities.UsuarioDomain;
import com.postechfiap.meumenu.core.domain.entities.EnderecoRestauranteDomain;
import com.postechfiap.meumenu.core.domain.entities.HorarioFuncionamentoDomain;
import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.core.domain.entities.TipoCozinhaDomain;
import com.postechfiap.meumenu.core.domain.entities.enums.DiaSemanaEnum;
import com.postechfiap.meumenu.core.domain.entities.enums.StatusContaEnum;
import com.postechfiap.meumenu.infrastructure.data.datamappers.ProprietarioDataMapper;
import com.postechfiap.meumenu.infrastructure.data.datamappers.UsuarioDataMapper;
import com.postechfiap.meumenu.infrastructure.model.RestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.ProprietarioEntity;
import com.postechfiap.meumenu.infrastructure.model.UsuarioEntity;
import com.postechfiap.meumenu.infrastructure.model.EnderecoRestauranteEntity;
import com.postechfiap.meumenu.infrastructure.model.HorarioFuncionamentoEntity;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import com.postechfiap.meumenu.infrastructure.model.TipoCozinhaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteDataMapperImplTest {

    @Mock
    private ProprietarioDataMapper proprietarioDataMapper;
    @Mock
    private UsuarioDataMapper usuarioDataMapper; // Necessário para o mapeamento de EnderecoDomain/Entity

    @InjectMocks
    private RestauranteDataMapperImpl restauranteDataMapper;

    // Domínios e Entidades de Exemplo
    private UUID restauranteId;
    private UUID proprietarioId;
    private UUID enderecoRestauranteId;
    private UUID tipoCozinhaId;
    private UUID horarioId;
    private UUID itemId;
    private UUID usuarioParaEnderecoId;

    private RestauranteDomain restauranteDomain;
    private ProprietarioDomain proprietarioDomain;
    private EnderecoRestauranteDomain enderecoRestauranteDomain;
    private HorarioFuncionamentoDomain horarioFuncionamentoDomain;
    private TipoCozinhaDomain tipoCozinhaDomain;
    private ItemCardapioDomain itemCardapioDomain;
    private UsuarioDomain usuarioDomainParaEndereco;

    private RestauranteEntity restauranteEntity;
    private ProprietarioEntity proprietarioEntity;
    private EnderecoRestauranteEntity enderecoRestauranteEntity;
    private HorarioFuncionamentoEntity horarioFuncionamentoEntity;
    private TipoCozinhaEntity tipoCozinhaEntity;
    private ItemCardapioEntity itemCardapioEntity;
    private UsuarioEntity usuarioEntityParaEndereco;


    @BeforeEach
    void setUp() {
        // --- IDs ---
        restauranteId = UUID.randomUUID();
        proprietarioId = UUID.randomUUID();
        enderecoRestauranteId = UUID.randomUUID();
        tipoCozinhaId = UUID.randomUUID();
        horarioId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        usuarioParaEnderecoId = UUID.randomUUID();

        // --- Instâncias de Domínio ---
        usuarioDomainParaEndereco = new UsuarioDomain(
                usuarioParaEnderecoId, "End User", "end@user.com", "enduser", "hash",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList()
        );

        proprietarioDomain = new ProprietarioDomain(
                proprietarioId, "12345678900", "11999999999", StatusContaEnum.ATIVO,
                "Prop Teste", "prop@test.com", "prop.login", "prop_hash",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList(), Collections.emptyList()
        );

        enderecoRestauranteDomain = new EnderecoRestauranteDomain(
                enderecoRestauranteId, "SP", "Sao Paulo", "Jardins", "Rua X", 123, "Apto 1", "01000000",
                null // RestauranteDomain será setado depois
        );

        tipoCozinhaDomain = new TipoCozinhaDomain(tipoCozinhaId, "Italiana");

        horarioFuncionamentoDomain = new HorarioFuncionamentoDomain(
                horarioId, LocalTime.of(9,0), LocalTime.of(18,0), DiaSemanaEnum.SEGUNDA_FEIRA,
                null // RestauranteDomain será setado depois
        );

        itemCardapioDomain = new ItemCardapioDomain(
                itemId, "Pizza", "Mussarela", BigDecimal.valueOf(50.00), false, "url_pizza",
                null // RestauranteDomain será setado depois
        );

        // RestauranteDomain completo (com relações bidirecionais no domínio)
        restauranteDomain = new RestauranteDomain(
                restauranteId, "00000000000100", "Razao Social Teste", "Restaurante Fantasia",
                "IE Teste", "11987654321", proprietarioDomain,
                enderecoRestauranteDomain,
                Collections.singletonList(tipoCozinhaDomain),
                Collections.singletonList(horarioFuncionamentoDomain),
                Collections.singletonList(itemCardapioDomain)
        );
        // Configurar as relações bidirecionais no domínio
        enderecoRestauranteDomain.setRestaurante(restauranteDomain);
        horarioFuncionamentoDomain.setRestaurante(restauranteDomain);
        itemCardapioDomain.setRestaurante(restauranteDomain);


        // --- Instâncias de Entidade ---
        usuarioEntityParaEndereco = new UsuarioEntity();
        usuarioEntityParaEndereco.setId(usuarioParaEnderecoId); // Mock do ID
        // Não precisamos de todos os campos para o mapeamento do endereço

        proprietarioEntity = new ProprietarioEntity(
                proprietarioId,
                "Prop Teste",
                "prop@test.com",
                "prop.login",
                "prop_hash",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(5),
                Collections.emptyList(),
                "12345678900",
                "11999999999",
                StatusContaEnum.ATIVO,
                null // Lista de Restaurantes será setada depois
        );

        enderecoRestauranteEntity = new EnderecoRestauranteEntity(
                enderecoRestauranteId, "SP", "Sao Paulo", "Jardins", "Rua X", 123, "Apto 1", "01000000",
                null // RestauranteEntity será setado depois
        );

        tipoCozinhaEntity = new TipoCozinhaEntity(tipoCozinhaId, "Italiana");

        horarioFuncionamentoEntity = new HorarioFuncionamentoEntity(
                horarioId, LocalTime.of(9,0), LocalTime.of(18,0), DiaSemanaEnum.SEGUNDA_FEIRA,
                null // RestauranteEntity será setado depois
        );

        itemCardapioEntity = new ItemCardapioEntity(
                itemId, "Pizza", "Mussarela", BigDecimal.valueOf(50.00), false, "url_pizza",
                null // RestauranteEntity será setado depois
        );

        // RestauranteEntity completo (com relações bidirecionais na entidade)
        restauranteEntity = new RestauranteEntity(
                restauranteId, "00000000000100", "Razao Social Teste", "Restaurante Fantasia",
                "IE Teste", "11987654321", proprietarioEntity,
                enderecoRestauranteEntity,
                Collections.singletonList(tipoCozinhaEntity),
                Collections.singletonList(horarioFuncionamentoEntity),
                Collections.singletonList(itemCardapioEntity)
        );
        // Configurar as relações bidirecionais na Entity
        enderecoRestauranteEntity.setRestaurante(restauranteEntity);
        horarioFuncionamentoEntity.setRestaurante(restauranteEntity);
        itemCardapioEntity.setRestaurante(restauranteEntity);

        // --- Mockando os Mappers Aninhados ---
        // Mocka proprietarioDataMapper.toEntity/toDomain
    }

    // --- TESTES DE MAPEAMENTO toEntity ---

    @Test
    @DisplayName("Deve mapear RestauranteDomain completo para RestauranteEntity corretamente")
    void toEntity_shouldMapFullRestauranteDomainToRestauranteEntityCorrectly() {
        RestauranteEntity mappedEntity = restauranteDataMapper.toEntity(restauranteDomain);

        assertNotNull(mappedEntity);
        assertEquals(restauranteDomain.getId(), mappedEntity.getId());
        assertEquals(restauranteDomain.getCnpj(), mappedEntity.getCnpj());
        assertEquals(restauranteDomain.getNomeFantasia(), mappedEntity.getNomeFantasia());
        assertEquals(restauranteDomain.getTelefoneComercial(), mappedEntity.getTelefoneComercial());

        // Verifica EnderecoRestaurante
        assertNotNull(mappedEntity.getEndereco());
        assertEquals(enderecoRestauranteDomain.getId(), mappedEntity.getEndereco().getId());
        assertEquals(mappedEntity, mappedEntity.getEndereco().getRestaurante()); // Verifica a relação inversa

        // Verifica Tipos de Cozinha
        assertNotNull(mappedEntity.getTiposCozinha());
        assertFalse(mappedEntity.getTiposCozinha().isEmpty());
        assertEquals(1, mappedEntity.getTiposCozinha().size());
        assertEquals(tipoCozinhaDomain.getId(), mappedEntity.getTiposCozinha().get(0).getId());

        // Verifica Horários de Funcionamento
        assertNotNull(mappedEntity.getHorariosFuncionamento());
        assertFalse(mappedEntity.getHorariosFuncionamento().isEmpty());
        assertEquals(1, mappedEntity.getHorariosFuncionamento().size());
        assertEquals(horarioFuncionamentoDomain.getId(), mappedEntity.getHorariosFuncionamento().get(0).getId());
        assertEquals(mappedEntity, mappedEntity.getHorariosFuncionamento().get(0).getRestaurante()); // Verifica a relação inversa

        // Verifica Itens do Cardápio
        assertNotNull(mappedEntity.getItensCardapio());
        assertFalse(mappedEntity.getItensCardapio().isEmpty());
        assertEquals(1, mappedEntity.getItensCardapio().size());
        assertEquals(itemCardapioDomain.getId(), mappedEntity.getItensCardapio().get(0).getId());
        assertEquals(mappedEntity, mappedEntity.getItensCardapio().get(0).getRestaurante()); // Verifica a relação inversa
    }

    @Test
    @DisplayName("Deve retornar null quando RestauranteDomain é null em toEntity")
    void toEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(restauranteDataMapper.toEntity(null));
        verifyNoInteractions(proprietarioDataMapper, usuarioDataMapper); // Não deve chamar mappers aninhados
    }

    @Test
    @DisplayName("Deve mapear RestauranteDomain com coleções nulas para RestauranteEntity corretamente")
    void toEntity_shouldHandleNullCollections() {
        restauranteDomain = new RestauranteDomain(
                restauranteId, "00000000000200", "RS Nulls", "NomeF Nulls", "IE", "Tel",
                proprietarioDomain, null, null, null, null // Coleções e Endereço nulos
        );
        RestauranteEntity mappedEntity = restauranteDataMapper.toEntity(restauranteDomain);

        assertNotNull(mappedEntity);
        assertNull(mappedEntity.getEndereco());
        assertNotNull(mappedEntity.getTiposCozinha()); assertTrue(mappedEntity.getTiposCozinha().isEmpty());
        assertNotNull(mappedEntity.getHorariosFuncionamento()); assertTrue(mappedEntity.getHorariosFuncionamento().isEmpty());
        assertNotNull(mappedEntity.getItensCardapio()); assertTrue(mappedEntity.getItensCardapio().isEmpty());
    }


    // --- TESTES DE MAPEAMENTO toDomain ---

    @Test
    @DisplayName("Deve mapear RestauranteEntity completo para RestauranteDomain corretamente")
    void toDomain_shouldMapFullRestauranteEntityToRestauranteDomainCorrectly() {
        RestauranteDomain mappedDomain = restauranteDataMapper.toDomain(restauranteEntity);

        assertNotNull(mappedDomain);
        assertEquals(restauranteEntity.getId(), mappedDomain.getId());
        assertEquals(restauranteEntity.getCnpj(), mappedDomain.getCnpj());
        assertEquals(restauranteEntity.getNomeFantasia(), mappedDomain.getNomeFantasia());
        assertEquals(restauranteEntity.getTelefoneComercial(), mappedDomain.getTelefoneComercial());

        // Verifica EnderecoRestaurante
        assertNotNull(mappedDomain.getEndereco());
        assertEquals(enderecoRestauranteEntity.getId(), mappedDomain.getEndereco().getId());
        assertEquals(mappedDomain, mappedDomain.getEndereco().getRestaurante()); // Verifica a relação inversa

        // Verifica Tipos de Cozinha
        assertNotNull(mappedDomain.getTiposCozinha());
        assertFalse(mappedDomain.getTiposCozinha().isEmpty());
        assertEquals(1, mappedDomain.getTiposCozinha().size());
        assertEquals(tipoCozinhaEntity.getId(), mappedDomain.getTiposCozinha().get(0).getId());

        // Verifica Horários de Funcionamento
        assertNotNull(mappedDomain.getHorariosFuncionamento());
        assertFalse(mappedDomain.getHorariosFuncionamento().isEmpty());
        assertEquals(1, mappedDomain.getHorariosFuncionamento().size());
        assertEquals(horarioFuncionamentoEntity.getId(), mappedDomain.getHorariosFuncionamento().get(0).getId());
        assertEquals(mappedDomain, mappedDomain.getHorariosFuncionamento().get(0).getRestaurante()); // Verifica a relação inversa

        // Verifica Itens do Cardápio
        assertNotNull(mappedDomain.getItensCardapio());
        assertFalse(mappedDomain.getItensCardapio().isEmpty());
        assertEquals(1, mappedDomain.getItensCardapio().size());
        assertEquals(itemCardapioEntity.getId(), mappedDomain.getItensCardapio().get(0).getId());
        assertEquals(mappedDomain, mappedDomain.getItensCardapio().get(0).getRestaurante()); // Verifica a relação inversa
    }

    @Test
    @DisplayName("Deve retornar null quando RestauranteEntity é null em toDomain")
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(restauranteDataMapper.toDomain(null));
        verifyNoInteractions(proprietarioDataMapper, usuarioDataMapper); // Não deve chamar mappers aninhados
    }

    @Test
    @DisplayName("Deve mapear RestauranteEntity com coleções nulas para RestauranteDomain corretamente")
    void toDomain_shouldHandleNullCollections() {
        restauranteEntity = new RestauranteEntity(); // Cria um novo, vazio
        restauranteEntity.setId(restauranteId); // Seta ID para ser válido
        restauranteEntity.setCnpj("00000000000300");
        // ... outros campos básicos, coleções como null
        restauranteEntity.setEndereco(null);
        restauranteEntity.setTiposCozinha(null);
        restauranteEntity.setHorariosFuncionamento(null);
        restauranteEntity.setItensCardapio(null);

        RestauranteDomain mappedDomain = restauranteDataMapper.toDomain(restauranteEntity);

        assertNotNull(mappedDomain);
        assertNull(mappedDomain.getEndereco());
        assertNotNull(mappedDomain.getTiposCozinha()); assertTrue(mappedDomain.getTiposCozinha().isEmpty());
        assertNotNull(mappedDomain.getHorariosFuncionamento()); assertTrue(mappedDomain.getHorariosFuncionamento().isEmpty());
        assertNotNull(mappedDomain.getItensCardapio()); assertTrue(mappedDomain.getItensCardapio().isEmpty());
    }

    // --- TESTES DE MÉTODOS AUXILIARES (testados indiretamente ou para null-safety) ---

    @Test
    @DisplayName("toEnderecoRestauranteEntity deve retornar null se domain for null")
    void toEnderecoRestauranteEntity_shouldReturnNullIfDomainIsNull() {
        assertNull(restauranteDataMapper.toEnderecoRestauranteEntity(null));
    }

    @Test
    @DisplayName("toEnderecoRestauranteDomain deve retornar null se entity for null")
    void toEnderecoRestauranteDomain_shouldReturnNullIfEntityIsNull() {
        assertNull(restauranteDataMapper.toEnderecoRestauranteDomain(null));
    }

    @Test
    @DisplayName("toHorarioFuncionamentoEntity deve retornar null se domain for null")
    void toHorarioFuncionamentoEntity_shouldReturnNullIfDomainIsNull() {
        assertNull(restauranteDataMapper.toHorarioFuncionamentoEntity(null));
    }

    @Test
    @DisplayName("toHorarioFuncionamentoDomain deve retornar null se entity for null")
    void toHorarioFuncionamentoDomain_shouldReturnNullIfEntityIsNull() {
        assertNull(restauranteDataMapper.toHorarioFuncionamentoDomain(null));
    }

    @Test
    @DisplayName("toTipoCozinhaEntity deve retornar null se domain for null")
    void toTipoCozinhaEntity_shouldReturnNullIfDomainIsNull() {
        assertNull(restauranteDataMapper.toTipoCozinhaEntity(null));
    }

    @Test
    @DisplayName("toTipoCozinhaDomain deve retornar null se entity for null")
    void toTipoCozinhaDomain_shouldReturnNullIfEntityIsNull() {
        assertNull(restauranteDataMapper.toTipoCozinhaDomain(null));
    }

    @Test
    @DisplayName("toItemCardapioEntity deve retornar null se domain for null")
    void toItemCardapioEntity_shouldReturnNullIfDomainIsNull() {
        assertNull(restauranteDataMapper.toItemCardapioEntity(null));
    }

    @Test
    @DisplayName("toItemCardapioDomain deve retornar null se entity for null")
    void toItemCardapioDomain_shouldReturnNullIfEntityIsNull() {
        assertNull(restauranteDataMapper.toItemCardapioDomain(null));
    }

    @Test
    @DisplayName("toHorarioFuncionamentoEntityList deve retornar null se domainList for null")
    void toHorarioFuncionamentoEntityList_shouldReturnNullIfDomainListIsNull() {
        assertNull(restauranteDataMapper.toHorarioFuncionamentoEntityList(null));
    }

    @Test
    @DisplayName("toHorarioFuncionamentoDomainList deve retornar null se entityList for null")
    void toHorarioFuncionamentoDomainList_shouldReturnNullIfEntityListIsNull() {
        assertNull(restauranteDataMapper.toHorarioFuncionamentoDomainList(null));
    }

    @Test
    @DisplayName("toTipoCozinhaEntityList deve retornar null se domainList for null")
    void toTipoCozinhaEntityList_shouldReturnNullIfDomainListIsNull() {
        assertNull(restauranteDataMapper.toTipoCozinhaEntityList(null));
    }

    @Test
    @DisplayName("toTipoCozinhaDomainList deve retornar null se entityList for null")
    void toTipoCozinhaDomainList_shouldReturnNullIfEntityListIsNull() {
        assertNull(restauranteDataMapper.toTipoCozinhaDomainList(null));
    }

    @Test
    @DisplayName("toItemCardapioEntityList deve retornar null se domainList for null")
    void toItemCardapioEntityList_shouldReturnNullIfDomainListIsNull() {
        assertNull(restauranteDataMapper.toItemCardapioEntityList(null));
    }

    @Test
    @DisplayName("toItemCardapioDomainList deve retornar null se entityList for null")
    void toItemCardapioDomainList_shouldReturnNullIfEntityListIsNull() {
        assertNull(restauranteDataMapper.toItemCardapioDomainList(null));
    }
}