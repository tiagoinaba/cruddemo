package com.tkinaba.solr.cruddemo.dao;

import com.tkinaba.solr.cruddemo.entity.Employee;
import com.tkinaba.solr.cruddemo.rest.EmployeeIdFormatException;
import com.tkinaba.solr.cruddemo.rest.EmployeeNotFoundException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

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
        queryParamMap.put("sort", "id_sort asc");
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        final QueryResponse response = solrClient.query(queryParams);
        final List<Employee> employees = response.getBeans(Employee.class);

        return employees;
    }

    @Override
    public Employee findById(int employeeId) throws SolrServerException, IOException {

        final Map<String, String> queryCheck = new HashMap<>();
        queryCheck.put("q", "*:*");
        queryCheck.put("sort", "id_sort desc");
        MapSolrParams paramsCheck = new MapSolrParams(queryCheck);
        final QueryResponse responseCheck = solrClient.query(paramsCheck);
        List<Employee> employeesCheck = responseCheck.getBeans(Employee.class);

        if( (employeeId > parseInt(employeesCheck.get(0).getId())) || (employeeId < 0) ) {
            throw new EmployeeNotFoundException("Employee id not found - " + employeeId);
        }

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
    public Employee deleteEmployeeById(int employeeId) throws SolrServerException, IOException {

        Employee employee = findById(employeeId);
        solrClient.deleteById(String.valueOf(employeeId));
        solrClient.commit();

        return employee;
    }

    @Override
    public Employee save(Employee employee) throws SolrServerException, IOException {
        if(employee.getId() == "0") {
            final Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("q", "*:*");
            queryParamMap.put("sort", "id_sort desc");
            queryParamMap.put("fl", "id");
            MapSolrParams queryParams = new MapSolrParams(queryParamMap);

            final QueryResponse response = solrClient.query(queryParams);
            final List<Employee> employees = response.getBeans(Employee.class);
            if(employees.size() > 0) {
                int newId = parseInt(employees.get(0).getId());
                employee.setId(Integer.toString(newId + 1));
            } else {
                employee.setId("1");
            }

        }

        try {
            int employeeId = parseInt(employee.getId());
        } catch (NumberFormatException exc) {
            throw new EmployeeIdFormatException("Employee id has to be an integer");
        }

        solrClient.addBean(employee);
        solrClient.commit();
        return employee;
    }
}
