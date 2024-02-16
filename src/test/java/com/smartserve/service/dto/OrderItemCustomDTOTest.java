package com.smartserve.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemCustomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemCustomDTO.class);
        OrderItemCustomDTO orderItemCustomDTO1 = new OrderItemCustomDTO();
        orderItemCustomDTO1.setId(1L);
        OrderItemCustomDTO orderItemCustomDTO2 = new OrderItemCustomDTO();
        assertThat(orderItemCustomDTO1).isNotEqualTo(orderItemCustomDTO2);
        orderItemCustomDTO2.setId(orderItemCustomDTO1.getId());
        assertThat(orderItemCustomDTO1).isEqualTo(orderItemCustomDTO2);
        orderItemCustomDTO2.setId(2L);
        assertThat(orderItemCustomDTO1).isNotEqualTo(orderItemCustomDTO2);
        orderItemCustomDTO1.setId(null);
        assertThat(orderItemCustomDTO1).isNotEqualTo(orderItemCustomDTO2);
    }
}
