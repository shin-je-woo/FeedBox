package com.feedbox.infrastructure.elasticsearch.config;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig {

    @Value("${spring.data.elasticsearch.host}")
    private String esHost;

    @Value("${spring.data.elasticsearch.port}")
    private int esPort;

    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost(esHost, esPort))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setDefaultHeaders(
                            List.of(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"))
                    );
                    httpClientBuilder.addInterceptorLast((HttpResponseInterceptor)
                            (response, context) -> response.addHeader("X-Elastic-Product", "Elasticsearch")
                    );
                    return httpClientBuilder;
                }).build();
    }
}
