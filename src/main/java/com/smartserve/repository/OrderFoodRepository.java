package com.smartserve.repository;

import com.smartserve.domain.OrderFood;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderFood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderFoodRepository extends JpaRepository<OrderFood, Long> {}
