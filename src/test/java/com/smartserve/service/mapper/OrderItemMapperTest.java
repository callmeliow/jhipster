package com.smartserve.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OrderItemMapperTest {

    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void setUp() {
        orderItemMapper = new OrderItemMapperImpl();
    }
}
