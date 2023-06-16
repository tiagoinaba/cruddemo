package com.tkinaba.solr.cruddemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkinaba.solr.cruddemo.dao.EmployeeDAO;
import com.tkinaba.solr.cruddemo.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = EmployeeRestController.class)
@AutoConfigureMockMvc
class EmployeeRestControllerTest {

    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private EmployeeDAO employeeDAO;


    Employee mockEmployee_1 = new Employee("1", "Tiago", "Inaba", "CEO",
            "tiago.inaba@gmail.com", "4444-4444", 500000);
    Employee mockEmployee_2 = new Employee("2", "Walter", "White", "Chem Teacher", "walter@gmail.com",
            "1234-56789", 400000);
    Employee mockEmployee_3 = new Employee("3", "Saul", "Goodman", "Lawyer", "saul@yahoo.com",
            "1235-78649", 90000);

    String exampleEmployeeJson = "{\"id\":\"1\",\"firstName\":\"Tiago\",\"lastName\":\"Inaba\",\"jobTitle\":\"CEO\",\"emailAddress\":" +
            "\"tiago.inaba@gmail.com\",\"phoneNumber\":\"4444-4444\",\"salary\":500000.0}";

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getAllShouldReturn200StatusResponse() throws Exception {
        List<Employee> employees = new ArrayList<>(Arrays.asList(mockEmployee_1, mockEmployee_2, mockEmployee_3));

        Mockito.when(employeeDAO.findAll()).thenReturn(employees);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/employees")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void getByIdShouldReturn200Response() throws Exception {
        List<Employee> employees = new ArrayList<>(Arrays.asList(mockEmployee_1, mockEmployee_2, mockEmployee_3));

        Mockito.when(employeeDAO.findById(2)).thenReturn(mockEmployee_2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/employees/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void addEmployeeShouldReturn201Response() throws Exception {
        Mockito.when(employeeDAO.save(any(Employee.class))).thenReturn(mockEmployee_2);

        String requestBody = objectMapper.writeValueAsString(mockEmployee_2);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees").contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    public void updateEmployeeShouldReturn200Response() throws Exception {
        Mockito.when(employeeDAO.save(any(Employee.class))).thenReturn(mockEmployee_2);

        String requestBody = objectMapper.writeValueAsString(mockEmployee_2);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteEmployeeShouldReturn200Response() throws Exception {
        Mockito.when(employeeDAO.deleteEmployeeById(2)).thenReturn(mockEmployee_2);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void getByIdShouldReturn400() throws Exception {
        Mockito.when(employeeDAO.findById(any(Integer.class))).thenThrow(new EmployeeIdFormatException("Employee id has to be an integer"));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void putRequestShouldReturn400() throws Exception {
        Mockito.when(employeeDAO.save(any(Employee.class))).thenThrow(new EmployeeIdFormatException("Employee id has to be an integer"));

        String requestBody = objectMapper.writeValueAsString(mockEmployee_2);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

}