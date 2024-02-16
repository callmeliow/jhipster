package com.smartserve.service.mapper;

import com.smartserve.domain.Order;
import com.smartserve.domain.OrderFood;
import com.smartserve.service.dto.OrderDTO;
import com.smartserve.service.dto.OrderFoodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderFood} and its DTO {@link OrderFoodDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderFoodMapper extends EntityMapper<OrderFoodDTO, OrderFood> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderFoodDTO toDto(OrderFood s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
