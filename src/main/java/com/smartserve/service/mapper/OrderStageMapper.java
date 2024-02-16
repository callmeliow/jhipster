package com.smartserve.service.mapper;

import com.smartserve.domain.OrderStage;
import com.smartserve.service.dto.OrderStageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStage} and its DTO {@link OrderStageDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderStageMapper extends EntityMapper<OrderStageDTO, OrderStage> {}
