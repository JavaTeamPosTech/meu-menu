package com.postechfiap.meumenu.infrastructure.data.datamappers.impl;

import com.postechfiap.meumenu.core.domain.entities.ItemCardapioDomain;
import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioDataMapperImplTest {

    @InjectMocks
    private ItemCardapioDataMapperImpl itemCardapioDataMapper;

    private UUID itemId;
    private ItemCardapioDomain itemCardapioDomain;
    private ItemCardapioEntity itemCardapioEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        itemId = UUID.randomUUID();

        itemCardapioDomain = new ItemCardapioDomain(
                itemId,
                "Lasanha Bolognesa",
                "Deliciosa lasanha com molho bolonhesa e queijo",
                BigDecimal.valueOf(65.50),
                true, // Disponível apenas no restaurante
                "http://restaurante.com/lasanha.jpg",
                null // RestauranteDomain é nulo aqui no domínio do item
        );

        itemCardapioEntity = new ItemCardapioEntity();
        itemCardapioEntity.setId(itemId);
        itemCardapioEntity.setNome("Lasanha Bolognesa");
        itemCardapioEntity.setDescricao("Deliciosa lasanha com molho bolonhesa e queijo");
        itemCardapioEntity.setPreco(BigDecimal.valueOf(65.50));
        itemCardapioEntity.setDisponivelApenasNoRestaurante(true);
        itemCardapioEntity.setUrlFoto("http://restaurante.com/lasanha.jpg");
        itemCardapioEntity.setRestaurante(null); // RestauranteEntity é nulo aqui na entidade
    }

    @Test
    @DisplayName("Deve mapear ItemCardapioDomain para ItemCardapioEntity corretamente")
    void toEntity_shouldMapItemCardapioDomainToItemCardapioEntityCorrectly() {
        ItemCardapioEntity mappedEntity = itemCardapioDataMapper.toEntity(itemCardapioDomain);

        assertNotNull(mappedEntity);
        assertEquals(itemCardapioDomain.getId(), mappedEntity.getId());
        assertEquals(itemCardapioDomain.getNome(), mappedEntity.getNome());
        assertEquals(itemCardapioDomain.getDescricao(), mappedEntity.getDescricao());
        assertEquals(itemCardapioDomain.getPreco(), mappedEntity.getPreco());
        assertEquals(itemCardapioDomain.getDisponivelApenasNoRestaurante(), mappedEntity.getDisponivelApenasNoRestaurante());
        assertEquals(itemCardapioDomain.getUrlFoto(), mappedEntity.getUrlFoto());
        assertNull(mappedEntity.getRestaurante()); // Restaurante deve ser nulo no mapeamento direto do item
    }

    @Test
    @DisplayName("Deve mapear ItemCardapioEntity para ItemCardapioDomain corretamente")
    void toDomain_shouldMapItemCardapioEntityToItemCardapioDomainCorrectly() {
        ItemCardapioDomain mappedDomain = itemCardapioDataMapper.toDomain(itemCardapioEntity);

        assertNotNull(mappedDomain);
        assertEquals(itemCardapioEntity.getId(), mappedDomain.getId());
        assertEquals(itemCardapioEntity.getNome(), mappedDomain.getNome());
        assertEquals(itemCardapioEntity.getDescricao(), mappedDomain.getDescricao());
        assertEquals(itemCardapioEntity.getPreco(), mappedDomain.getPreco());
        assertEquals(itemCardapioEntity.getDisponivelApenasNoRestaurante(), mappedDomain.getDisponivelApenasNoRestaurante());
        assertEquals(itemCardapioEntity.getUrlFoto(), mappedDomain.getUrlFoto());
        assertNull(mappedDomain.getRestaurante()); // Restaurante deve ser nulo no mapeamento direto do item
    }

    @Test
    @DisplayName("Deve retornar null quando ItemCardapioDomain é null em toEntity")
    void toEntity_shouldReturnNullWhenDomainIsNull() {
        assertNull(itemCardapioDataMapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve retornar null quando ItemCardapioEntity é null em toDomain")
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertNull(itemCardapioDataMapper.toDomain(null));
    }

    @Test
    @DisplayName("Deve mapear ItemCardapioDomain com campos nulos para ItemCardapioEntity corretamente")
    void toEntity_shouldHandleNullFields() {
        ItemCardapioDomain domainWithNulls = new ItemCardapioDomain(
                UUID.randomUUID(), "Item Null", null, null, null, null, null
        );
        ItemCardapioEntity mappedEntity = itemCardapioDataMapper.toEntity(domainWithNulls);

        assertNotNull(mappedEntity);
        assertEquals(domainWithNulls.getId(), mappedEntity.getId());
        assertEquals(domainWithNulls.getNome(), mappedEntity.getNome());
        assertNull(mappedEntity.getDescricao());
        assertNull(mappedEntity.getPreco());
        assertNull(mappedEntity.getDisponivelApenasNoRestaurante());
        assertNull(mappedEntity.getUrlFoto());
    }

    @Test
    @DisplayName("Deve mapear ItemCardapioEntity com campos nulos para ItemCardapioDomain corretamente")
    void toDomain_shouldHandleNullFields() {
        ItemCardapioEntity entityWithNulls = new ItemCardapioEntity();
        entityWithNulls.setId(UUID.randomUUID());
        entityWithNulls.setNome("Entity Null");

        ItemCardapioDomain mappedDomain = itemCardapioDataMapper.toDomain(entityWithNulls);

        assertNotNull(mappedDomain);
        assertEquals(entityWithNulls.getId(), mappedDomain.getId());
        assertEquals(entityWithNulls.getNome(), mappedDomain.getNome());
        assertNull(mappedDomain.getDescricao());
        assertNull(mappedDomain.getPreco());
        assertNull(mappedDomain.getDisponivelApenasNoRestaurante());
        assertNull(mappedDomain.getUrlFoto());
    }
}