package com.smartserve.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartserve.domain.OrderCustom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCustomDTO implements Serializable {

    private Long id;

    private String customizationName;

    private BigDecimal price;

    private OrderFoodDTO orderFood;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomizationName() {
        return customizationName;
    }

    public void setCustomizationName(String customizationName) {
        this.customizationName = customizationName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderFoodDTO getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(OrderFoodDTO orderFood) {
        this.orderFood = orderFood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderCustomDTO)) {
            return false;
        }

        OrderCustomDTO orderCustomDTO = (OrderCustomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderCustomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCustomDTO{" +
            "id=" + getId() +
            ", customizationName='" + getCustomizationName() + "'" +
            ", price=" + getPrice() +
            ", orderFood=" + getOrderFood() +
            "}";
    }
}
