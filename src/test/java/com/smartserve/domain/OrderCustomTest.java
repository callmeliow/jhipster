package com.smartserve.domain;

import static com.smartserve.domain.OrderCustomTestSamples.*;
import static com.smartserve.domain.OrderFoodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderCustomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderCustom.class);
        OrderCustom orderCustom1 = getOrderCustomSample1();
        OrderCustom orderCustom2 = new OrderCustom();
        assertThat(orderCustom1).isNotEqualTo(orderCustom2);

        orderCustom2.setId(orderCustom1.getId());
        assertThat(orderCustom1).isEqualTo(orderCustom2);

        orderCustom2 = getOrderCustomSample2();
        assertThat(orderCustom1).isNotEqualTo(orderCustom2);
    }

    @Test
    void orderFoodTest() throws Exception {
        OrderCustom orderCustom = getOrderCustomRandomSampleGenerator();
        OrderFood orderFoodBack = getOrderFoodRandomSampleGenerator();

        orderCustom.setOrderFood(orderFoodBack);
        assertThat(orderCustom.getOrderFood()).isEqualTo(orderFoodBack);

        orderCustom.orderFood(null);
        assertThat(orderCustom.getOrderFood()).isNull();
    }
}
