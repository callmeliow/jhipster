package com.smartserve.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FoodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Food getFoodSample1() {
        return new Food().id(1L).name("name1").description("description1").imageUrl("imageUrl1");
    }

    public static Food getFoodSample2() {
        return new Food().id(2L).name("name2").description("description2").imageUrl("imageUrl2");
    }

    public static Food getFoodRandomSampleGenerator() {
        return new Food()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString());
    }
}
