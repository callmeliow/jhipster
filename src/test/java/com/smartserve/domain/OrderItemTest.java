package com.smartserve.domain;

import static com.smartserve.domain.FoodTestSamples.*;
import static com.smartserve.domain.OrderItemTestSamples.*;
import static com.smartserve.domain.OrderStageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void foodTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Food foodBack = getFoodRandomSampleGenerator();

        orderItem.setFood(foodBack);
        assertThat(orderItem.getFood()).isEqualTo(foodBack);

        orderItem.food(null);
        assertThat(orderItem.getFood()).isNull();
    }

    @Test
    void orderStageTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        OrderStage orderStageBack = getOrderStageRandomSampleGenerator();

        orderItem.setOrderStage(orderStageBack);
        assertThat(orderItem.getOrderStage()).isEqualTo(orderStageBack);

        orderItem.orderStage(null);
        assertThat(orderItem.getOrderStage()).isNull();
    }
}
