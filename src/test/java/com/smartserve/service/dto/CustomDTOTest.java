package com.smartserve.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomDTO.class);
        CustomDTO customDTO1 = new CustomDTO();
        customDTO1.setId(1L);
        CustomDTO customDTO2 = new CustomDTO();
        assertThat(customDTO1).isNotEqualTo(customDTO2);
        customDTO2.setId(customDTO1.getId());
        assertThat(customDTO1).isEqualTo(customDTO2);
        customDTO2.setId(2L);
        assertThat(customDTO1).isNotEqualTo(customDTO2);
        customDTO1.setId(null);
        assertThat(customDTO1).isNotEqualTo(customDTO2);
    }
}
