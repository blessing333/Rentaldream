package com.blessing.rentaldream.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {
    private String host;
    public static Integer LIST_ITEM_COUNT = 9;
}