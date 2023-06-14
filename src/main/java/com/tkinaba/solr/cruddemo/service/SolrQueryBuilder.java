package com.tkinaba.solr.cruddemo.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolrQueryBuilder {

    public SolrDocumentList solrQuery(String webQuery) {
        SolrDocumentList list = null;
        List<String> zkServers = new ArrayList<>();
        zkServers.add("http://localhost:8983/solr");
        zkServers.add("http://localhost:7574/solr");

        try {
            CloudSolrClient solrClient = new CloudSolrClient.Builder(zkServers).build();
            solrClient.setDefaultCollection("employees");

            SolrQuery query = new SolrQuery();

            query.setQuery(webQuery);

            String finalQuery = query.toString();
            System.out.println("Final query is: " + finalQuery);
            QueryResponse response = solrClient.query(query);

            list = response.getResults();

            list.stream().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
        SolrQueryBuilder builder = new SolrQueryBuilder();

        builder.solrQuery("*");
    }

}
