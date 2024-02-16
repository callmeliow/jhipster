package com.smartserve.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CustomMapperTest {

    private CustomMapper customMapper;

    @BeforeEach
    public void setUp() {
        customMapper = new CustomMapperImpl();
    }
}
