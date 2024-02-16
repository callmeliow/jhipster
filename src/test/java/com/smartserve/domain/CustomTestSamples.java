package com.smartserve.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Custom getCustomSample1() {
        return new Custom().id(1L).ingredientName("ingredientName1").imageUrl("imageUrl1");
    }

    public static Custom getCustomSample2() {
        return new Custom().id(2L).ingredientName("ingredientName2").imageUrl("imageUrl2");
    }

    public static Custom getCustomRandomSampleGenerator() {
        return new Custom()
            .id(longCount.incrementAndGet())
            .ingredientName(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString());
    }
}
