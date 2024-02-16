package com.smartserve.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OrderFoodMapperTest {

    private OrderFoodMapper orderFoodMapper;

    @BeforeEach
    public void setUp() {
        orderFoodMapper = new OrderFoodMapperImpl();
    }
}
