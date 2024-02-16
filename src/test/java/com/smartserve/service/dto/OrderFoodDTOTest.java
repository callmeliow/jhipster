package com.smartserve.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderFoodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderFoodDTO.class);
        OrderFoodDTO orderFoodDTO1 = new OrderFoodDTO();
        orderFoodDTO1.setId(1L);
        OrderFoodDTO orderFoodDTO2 = new OrderFoodDTO();
        assertThat(orderFoodDTO1).isNotEqualTo(orderFoodDTO2);
        orderFoodDTO2.setId(orderFoodDTO1.getId());
        assertThat(orderFoodDTO1).isEqualTo(orderFoodDTO2);
        orderFoodDTO2.setId(2L);
        assertThat(orderFoodDTO1).isNotEqualTo(orderFoodDTO2);
        orderFoodDTO1.setId(null);
        assertThat(orderFoodDTO1).isNotEqualTo(orderFoodDTO2);
    }
}
