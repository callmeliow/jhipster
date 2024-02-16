package com.smartserve.domain;

import static com.smartserve.domain.OrderStageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStage.class);
        OrderStage orderStage1 = getOrderStageSample1();
        OrderStage orderStage2 = new OrderStage();
        assertThat(orderStage1).isNotEqualTo(orderStage2);

        orderStage2.setId(orderStage1.getId());
        assertThat(orderStage1).isEqualTo(orderStage2);

        orderStage2 = getOrderStageSample2();
        assertThat(orderStage1).isNotEqualTo(orderStage2);
    }
}
