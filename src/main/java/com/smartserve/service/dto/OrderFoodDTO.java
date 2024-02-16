package com.smartserve.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartserve.domain.OrderFood} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderFoodDTO implements Serializable {

    private Long id;

    @NotNull
    private String foodName;

    private BigDecimal price;

    private BigDecimal orderItemPrice;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(BigDecimal orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderFoodDTO)) {
            return false;
        }

        OrderFoodDTO orderFoodDTO = (OrderFoodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderFoodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderFoodDTO{" +
            "id=" + getId() +
            ", foodName='" + getFoodName() + "'" +
            ", price=" + getPrice() +
            ", orderItemPrice=" + getOrderItemPrice() +
            ", order=" + getOrder() +
            "}";
    }
}
