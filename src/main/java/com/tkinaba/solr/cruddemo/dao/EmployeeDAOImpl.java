package com.tkinaba.solr.cruddemo.dao;

import com.tkinaba.solr.cruddemo.entity.Employee;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    CloudSolrClient solrClient;

    @Autowired
    public EmployeeDAOImpl(CloudSolrClient theSolrClient) {
        List<String> solrUrls = new ArrayList<>();
        solrUrls.add("http://localhost:8983/solr");
        solrUrls.add("http://localhost:7574/solr");
        theSolrClient = new CloudSolrClient.Builder(solrUrls).build();
        theSolrClient.setDefaultCollection("employees");
        this.solrClient = theSolrClient;
    }

    @Override
    public List<Employee> findAll() throws SolrServerException, IOException {
        final Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("q", "*:*");
        queryParamMap.put("sort", "id asc");
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        final QueryResponse response = solrClient.query(queryParams);
        final List<Employee> employees = response.getBeans(Employee.class);

        return employees;
    }

    @Override
    public Employee findById(int employeeId) throws SolrServerException, IOException {
        final Map<String, String> queryParamMap = new HashMap<>();
        StringBuilder queryString = new StringBuilder("id:");
        queryString.append(employeeId);
        queryParamMap.put("q", queryString.toString());
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        final QueryResponse response = solrClient.query(queryParams);
        final List<Employee> employees = response.getBeans(Employee.class);

        return employees.get(0);
    }

    @Override
    public String deleteEmployeeById(String employeeId) throws SolrServerException, IOException {

        solrClient.deleteById(employeeId);
        solrClient.commit();

        return "Deleted employee with id: " + employeeId;
    }

    @Override
    public Employee save(Employee employee) throws SolrServerException, IOException {
        if(employee.getId() == "0") {
            final Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("q", "*:*");
            MapSolrParams queryParams = new MapSolrParams(queryParamMap);

            final QueryResponse response = solrClient.query(queryParams);
            final int size = response.getResults().size();

            employee.setId(Integer.toString(size + 1));
        }
        final UpdateResponse response = solrClient.addBean(employee);
        solrClient.commit();
        return employee;
    }
}
