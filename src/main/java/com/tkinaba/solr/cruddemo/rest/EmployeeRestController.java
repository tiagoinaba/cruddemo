package com.tkinaba.solr.cruddemo.rest;

import com.tkinaba.solr.cruddemo.dao.EmployeeDAO;
import com.tkinaba.solr.cruddemo.entity.Employee;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
public class EmployeeRestController {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeRestController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @GetMapping("/employees")
    public ResponseEntity getAllEmployees() throws SolrServerException, IOException {

        return new ResponseEntity(employeeDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity getEmployeeById(@PathVariable String employeeId) throws SolrServerException, IOException {

        try {
            int test = Integer.parseInt(employeeId);
        } catch(NumberFormatException exc) {
            throw new EmployeeIdFormatException("Employee id has to be an integer");
        }

        int employeeIdAsInt = Integer.parseInt(employeeId);

        return new ResponseEntity(employeeDAO.findById(employeeIdAsInt), HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity addEmployee(@RequestBody Employee employee) throws SolrServerException, IOException {

        employee.setId("0");
        Employee createdEmployee = employeeDAO.save(employee);
        URI created = URI.create("/employees/" + createdEmployee.getId());

        return ResponseEntity.created(created).body(createdEmployee);
    }

    @PutMapping("/employees")
    public ResponseEntity updateEmployee(@RequestBody Employee employee) throws SolrServerException, IOException {
        return new ResponseEntity(employeeDAO.save(employee), HttpStatus.OK);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable String employeeId) throws SolrServerException, IOException {

        try {
            int test = Integer.parseInt(employeeId);
        } catch(NumberFormatException exc) {
            throw new EmployeeIdFormatException("Employee id has to be an integer");
        }

        return new ResponseEntity(employeeDAO.deleteEmployeeById(Integer.parseInt(employeeId)), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException exc) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(Exception exc) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeIdFormatException exc) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
