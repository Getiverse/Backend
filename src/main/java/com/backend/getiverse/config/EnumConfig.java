package com.backend.getiverse.config;

import com.backend.getiverse.enums.FilterType;
import com.backend.getiverse.model.PostType;
import graphql.schema.GraphQLEnumType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumConfig {

    @Bean
    public GraphQLEnumType PostTypeEnum() {
        return GraphQLEnumType.newEnum()
                .name("PostType")
                .description("supported posts type")
                .value("ARTICLE", PostType.ARTICLE, "article")
                .value("INSTANT", PostType.INSTANT, "instant")
                .build();
    }

    @Bean
    public GraphQLEnumType FilterTypeEnum() {
        return GraphQLEnumType.newEnum()
                .name("FilterType")
                .description("supported posts type")
                .value("USER_ID", FilterType.USER_ID, "user_id filter")
                .value("LIBRARY_ID", FilterType.LIBRARY_ID, "library filter")
                .value("ID", FilterType.INSTANT_ID, "id fitler")
                .build();
    }
}