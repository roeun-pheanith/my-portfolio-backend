package com.nith.flex.portfolio.config;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class JacksonConfiguration {
	// Define a consistent format for LocalDateTime that includes time
    // Using ISO_LOCAL_DATE_TIME is a good standard for T-separated date and time.
    // Or, if you specifically want "yyyy-MM-dd HH:mm:ss" as your API standard:
    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"; // Or "yyyy-MM-dd'T'HH:mm:ss" for ISO standard
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // Register JavaTimeModule to ensure Java 8 Date/Time types are supported
            builder.modules(new JavaTimeModule());

            // Disable writing dates as timestamps (e.g., 1672531200000)
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // --- Configure Serializers ---
            // For LocalDate (only date part)
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));
            // For LocalDateTime (date AND time, using the consistent format)
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));

            // --- Configure Deserializers ---
            // For LocalDate (only date part)
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));
            // For LocalDateTime (date AND time, using the consistent format)
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));
        };
    }
}
