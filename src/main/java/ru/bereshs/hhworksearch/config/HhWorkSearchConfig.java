package ru.bereshs.hhworksearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class HhWorkSearchConfig {

    @Bean
    DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm dd-MM-YYYY").withZone(ZoneId.systemDefault());
    }

}
