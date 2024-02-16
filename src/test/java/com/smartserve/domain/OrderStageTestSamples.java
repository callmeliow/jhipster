package com.smartserve.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderStageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderStage getOrderStageSample1() {
        return new OrderStage().id(1L).orderNo("orderNo1");
    }

    public static OrderStage getOrderStageSample2() {
        return new OrderStage().id(2L).orderNo("orderNo2");
    }

    public static OrderStage getOrderStageRandomSampleGenerator() {
        return new OrderStage().id(longCount.incrementAndGet()).orderNo(UUID.randomUUID().toString());
    }
}
