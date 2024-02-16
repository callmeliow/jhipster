package com.smartserve.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStageDTO.class);
        OrderStageDTO orderStageDTO1 = new OrderStageDTO();
        orderStageDTO1.setId(1L);
        OrderStageDTO orderStageDTO2 = new OrderStageDTO();
        assertThat(orderStageDTO1).isNotEqualTo(orderStageDTO2);
        orderStageDTO2.setId(orderStageDTO1.getId());
        assertThat(orderStageDTO1).isEqualTo(orderStageDTO2);
        orderStageDTO2.setId(2L);
        assertThat(orderStageDTO1).isNotEqualTo(orderStageDTO2);
        orderStageDTO1.setId(null);
        assertThat(orderStageDTO1).isNotEqualTo(orderStageDTO2);
    }
}
