package com.smartserve.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartserve.domain.OrderItemCustom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderItemCustomDTO implements Serializable {

    private Long id;

    private OrderItemDTO orderItem;

    private CustomDTO custom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemDTO orderItem) {
        this.orderItem = orderItem;
    }

    public CustomDTO getCustom() {
        return custom;
    }

    public void setCustom(CustomDTO custom) {
        this.custom = custom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemCustomDTO)) {
            return false;
        }

        OrderItemCustomDTO orderItemCustomDTO = (OrderItemCustomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemCustomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemCustomDTO{" +
            "id=" + getId() +
            ", orderItem=" + getOrderItem() +
            ", custom=" + getCustom() +
            "}";
    }
}
