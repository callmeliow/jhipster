package com.smartserve.domain;

import static com.smartserve.domain.OrderFoodTestSamples.*;
import static com.smartserve.domain.OrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderFoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderFood.class);
        OrderFood orderFood1 = getOrderFoodSample1();
        OrderFood orderFood2 = new OrderFood();
        assertThat(orderFood1).isNotEqualTo(orderFood2);

        orderFood2.setId(orderFood1.getId());
        assertThat(orderFood1).isEqualTo(orderFood2);

        orderFood2 = getOrderFoodSample2();
        assertThat(orderFood1).isNotEqualTo(orderFood2);
    }

    @Test
    void orderTest() throws Exception {
        OrderFood orderFood = getOrderFoodRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderFood.setOrder(orderBack);
        assertThat(orderFood.getOrder()).isEqualTo(orderBack);

        orderFood.order(null);
        assertThat(orderFood.getOrder()).isNull();
    }
}
