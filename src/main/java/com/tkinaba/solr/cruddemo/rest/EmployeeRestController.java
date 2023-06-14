package com.tkinaba.solr.cruddemo.rest;

import com.tkinaba.solr.cruddemo.dao.EmployeeDAO;
import com.tkinaba.solr.cruddemo.entity.Employee;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class EmployeeRestController {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeRestController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() throws SolrServerException, IOException {

        return employeeDAO.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployeeById(@PathVariable int employeeId) throws SolrServerException, IOException {
        return employeeDAO.findById(employeeId);
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee) throws SolrServerException, IOException {

        employee.setId("0");

        return employeeDAO.save(employee);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) throws SolrServerException, IOException {
        return employeeDAO.save(employee);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable String employeeId) throws SolrServerException, IOException {
        return employeeDAO.deleteEmployeeById(employeeId);
    }

}
