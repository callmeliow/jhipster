package com.smartserve.repository;

import com.smartserve.domain.OrderItemCustom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderItemCustom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemCustomRepository extends JpaRepository<OrderItemCustom, Long> {}
