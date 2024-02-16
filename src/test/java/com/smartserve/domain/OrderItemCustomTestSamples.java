package com.smartserve.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderItemCustomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderItemCustom getOrderItemCustomSample1() {
        return new OrderItemCustom().id(1L);
    }

    public static OrderItemCustom getOrderItemCustomSample2() {
        return new OrderItemCustom().id(2L);
    }

    public static OrderItemCustom getOrderItemCustomRandomSampleGenerator() {
        return new OrderItemCustom().id(longCount.incrementAndGet());
    }
}
