package com.smartserve.service.mapper;

import com.smartserve.domain.Custom;
import com.smartserve.domain.OrderItem;
import com.smartserve.domain.OrderItemCustom;
import com.smartserve.service.dto.CustomDTO;
import com.smartserve.service.dto.OrderItemCustomDTO;
import com.smartserve.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItemCustom} and its DTO {@link OrderItemCustomDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemCustomMapper extends EntityMapper<OrderItemCustomDTO, OrderItemCustom> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "orderItemId")
    @Mapping(target = "custom", source = "custom", qualifiedByName = "customId")
    OrderItemCustomDTO toDto(OrderItemCustom s);

    @Named("orderItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoOrderItemId(OrderItem orderItem);

    @Named("customId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomDTO toDtoCustomId(Custom custom);
}
