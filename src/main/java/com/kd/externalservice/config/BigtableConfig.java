package com.kd.externalservice.config;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BigtableConfig {

    @Bean
    public BigtableDataClient bigtableDataClient() throws IOException{

        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder()
                        .setProjectId("hidden-cosmos-374507")
                        .setInstanceId("demo-1")
                        .build();

        return BigtableDataClient.create(settings);

    }
}
