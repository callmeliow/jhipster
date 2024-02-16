package com.smartserve.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderCustomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderCustom getOrderCustomSample1() {
        return new OrderCustom().id(1L).customizationName("customizationName1");
    }

    public static OrderCustom getOrderCustomSample2() {
        return new OrderCustom().id(2L).customizationName("customizationName2");
    }

    public static OrderCustom getOrderCustomRandomSampleGenerator() {
        return new OrderCustom().id(longCount.incrementAndGet()).customizationName(UUID.randomUUID().toString());
    }
}
