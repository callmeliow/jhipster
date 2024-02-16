package com.smartserve.repository;

import com.smartserve.domain.OrderStage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderStage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderStageRepository extends JpaRepository<OrderStage, Long> {}
