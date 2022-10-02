package com.loudbook.minestom.impl.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {
    private ObjectMapper mapper;
    public Config() {
        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }
}
