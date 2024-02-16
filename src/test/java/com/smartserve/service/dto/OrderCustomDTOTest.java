package com.smartserve.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderCustomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderCustomDTO.class);
        OrderCustomDTO orderCustomDTO1 = new OrderCustomDTO();
        orderCustomDTO1.setId(1L);
        OrderCustomDTO orderCustomDTO2 = new OrderCustomDTO();
        assertThat(orderCustomDTO1).isNotEqualTo(orderCustomDTO2);
        orderCustomDTO2.setId(orderCustomDTO1.getId());
        assertThat(orderCustomDTO1).isEqualTo(orderCustomDTO2);
        orderCustomDTO2.setId(2L);
        assertThat(orderCustomDTO1).isNotEqualTo(orderCustomDTO2);
        orderCustomDTO1.setId(null);
        assertThat(orderCustomDTO1).isNotEqualTo(orderCustomDTO2);
    }
}
