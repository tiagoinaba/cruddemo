package com.tkinaba.solr.cruddemo.config;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
public class SolrConfig {

    @Bean
    public CloudSolrClient solrClient() {
        List<String> solrUrls = new ArrayList<>();
        solrUrls.add("http://localhost:8983/solr");
        solrUrls.add("http://localhost:7574/solr");
        return new CloudSolrClient.Builder(solrUrls).build();
    }
}
