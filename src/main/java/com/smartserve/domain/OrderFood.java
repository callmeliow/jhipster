package com.smartserve.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderFood.
 */
@Entity
@Table(name = "order_food")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderFood implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "order_item_price", precision = 21, scale = 2)
    private BigDecimal orderItemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderFood id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return this.foodName;
    }

    public OrderFood foodName(String foodName) {
        this.setFoodName(foodName);
        return this;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public OrderFood price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOrderItemPrice() {
        return this.orderItemPrice;
    }

    public OrderFood orderItemPrice(BigDecimal orderItemPrice) {
        this.setOrderItemPrice(orderItemPrice);
        return this;
    }

    public void setOrderItemPrice(BigDecimal orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderFood order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderFood)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderFood) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderFood{" +
            "id=" + getId() +
            ", foodName='" + getFoodName() + "'" +
            ", price=" + getPrice() +
            ", orderItemPrice=" + getOrderItemPrice() +
            "}";
    }
}
