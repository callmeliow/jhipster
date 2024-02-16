package com.smartserve.domain;

import static com.smartserve.domain.CustomTestSamples.*;
import static com.smartserve.domain.OrderItemCustomTestSamples.*;
import static com.smartserve.domain.OrderItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemCustomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemCustom.class);
        OrderItemCustom orderItemCustom1 = getOrderItemCustomSample1();
        OrderItemCustom orderItemCustom2 = new OrderItemCustom();
        assertThat(orderItemCustom1).isNotEqualTo(orderItemCustom2);

        orderItemCustom2.setId(orderItemCustom1.getId());
        assertThat(orderItemCustom1).isEqualTo(orderItemCustom2);

        orderItemCustom2 = getOrderItemCustomSample2();
        assertThat(orderItemCustom1).isNotEqualTo(orderItemCustom2);
    }

    @Test
    void orderItemTest() throws Exception {
        OrderItemCustom orderItemCustom = getOrderItemCustomRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        orderItemCustom.setOrderItem(orderItemBack);
        assertThat(orderItemCustom.getOrderItem()).isEqualTo(orderItemBack);

        orderItemCustom.orderItem(null);
        assertThat(orderItemCustom.getOrderItem()).isNull();
    }

    @Test
    void customTest() throws Exception {
        OrderItemCustom orderItemCustom = getOrderItemCustomRandomSampleGenerator();
        Custom customBack = getCustomRandomSampleGenerator();

        orderItemCustom.setCustom(customBack);
        assertThat(orderItemCustom.getCustom()).isEqualTo(customBack);

        orderItemCustom.custom(null);
        assertThat(orderItemCustom.getCustom()).isNull();
    }
}
