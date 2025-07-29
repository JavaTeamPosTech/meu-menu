package com.postechfiap.meumenu.infrastructure.data.repositories;

import com.postechfiap.meumenu.infrastructure.model.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemCardapioSpringRepository extends JpaRepository<ItemCardapioEntity, UUID> {
}