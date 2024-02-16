package com.smartserve.repository;

import com.smartserve.domain.Custom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Custom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomRepository extends JpaRepository<Custom, Long> {}
