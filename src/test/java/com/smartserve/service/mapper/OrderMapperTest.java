package com.smartserve.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapperImpl();
    }
}
