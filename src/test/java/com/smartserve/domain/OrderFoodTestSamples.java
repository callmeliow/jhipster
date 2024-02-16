package com.smartserve.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderFoodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderFood getOrderFoodSample1() {
        return new OrderFood().id(1L).foodName("foodName1");
    }

    public static OrderFood getOrderFoodSample2() {
        return new OrderFood().id(2L).foodName("foodName2");
    }

    public static OrderFood getOrderFoodRandomSampleGenerator() {
        return new OrderFood().id(longCount.incrementAndGet()).foodName(UUID.randomUUID().toString());
    }
}
