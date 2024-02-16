package com.smartserve.service.mapper;

import com.smartserve.domain.Food;
import com.smartserve.domain.OrderItem;
import com.smartserve.domain.OrderStage;
import com.smartserve.service.dto.FoodDTO;
import com.smartserve.service.dto.OrderItemDTO;
import com.smartserve.service.dto.OrderStageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "food", source = "food", qualifiedByName = "foodId")
    @Mapping(target = "orderStage", source = "orderStage", qualifiedByName = "orderStageId")
    OrderItemDTO toDto(OrderItem s);

    @Named("foodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodDTO toDtoFoodId(Food food);

    @Named("orderStageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderStageDTO toDtoOrderStageId(OrderStage orderStage);
}
