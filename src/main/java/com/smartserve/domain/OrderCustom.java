package com.smartserve.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderCustom.
 */
@Entity
@Table(name = "order_custom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customization_name")
    private String customizationName;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private OrderFood orderFood;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderCustom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomizationName() {
        return this.customizationName;
    }

    public OrderCustom customizationName(String customizationName) {
        this.setCustomizationName(customizationName);
        return this;
    }

    public void setCustomizationName(String customizationName) {
        this.customizationName = customizationName;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public OrderCustom price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderFood getOrderFood() {
        return this.orderFood;
    }

    public void setOrderFood(OrderFood orderFood) {
        this.orderFood = orderFood;
    }

    public OrderCustom orderFood(OrderFood orderFood) {
        this.setOrderFood(orderFood);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderCustom)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderCustom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCustom{" +
            "id=" + getId() +
            ", customizationName='" + getCustomizationName() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
