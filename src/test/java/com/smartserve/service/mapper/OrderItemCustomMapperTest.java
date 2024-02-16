package com.smartserve.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OrderItemCustomMapperTest {

    private OrderItemCustomMapper orderItemCustomMapper;

    @BeforeEach
    public void setUp() {
        orderItemCustomMapper = new OrderItemCustomMapperImpl();
    }
}
