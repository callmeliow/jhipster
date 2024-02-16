package com.smartserve.repository;

import com.smartserve.domain.OrderCustom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderCustom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderCustomRepository extends JpaRepository<OrderCustom, Long> {}
