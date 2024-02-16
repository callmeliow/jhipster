package com.smartserve.service.mapper;

import com.smartserve.domain.OrderCustom;
import com.smartserve.domain.OrderFood;
import com.smartserve.service.dto.OrderCustomDTO;
import com.smartserve.service.dto.OrderFoodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderCustom} and its DTO {@link OrderCustomDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderCustomMapper extends EntityMapper<OrderCustomDTO, OrderCustom> {
    @Mapping(target = "orderFood", source = "orderFood", qualifiedByName = "orderFoodId")
    OrderCustomDTO toDto(OrderCustom s);

    @Named("orderFoodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderFoodDTO toDtoOrderFoodId(OrderFood orderFood);
}
