package com.tkinaba.solr.cruddemo.dao;

import com.tkinaba.solr.cruddemo.entity.Employee;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll() throws SolrServerException, IOException;
    Employee findById(int employeeId) throws SolrServerException, IOException;
    Employee save(Employee employee) throws SolrServerException, IOException;
    Employee deleteEmployeeById(int employeeId) throws SolrServerException, IOException;
}
