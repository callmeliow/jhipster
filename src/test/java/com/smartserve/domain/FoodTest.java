package com.smartserve.domain;

import static com.smartserve.domain.FoodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartserve.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Food.class);
        Food food1 = getFoodSample1();
        Food food2 = new Food();
        assertThat(food1).isNotEqualTo(food2);

        food2.setId(food1.getId());
        assertThat(food1).isEqualTo(food2);

        food2 = getFoodSample2();
        assertThat(food1).isNotEqualTo(food2);
    }
}
