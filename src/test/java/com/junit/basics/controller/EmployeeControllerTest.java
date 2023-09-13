package com.junit.basics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.basics.model.Employee;
import com.junit.basics.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Junit to create employee")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Tony")
                .lastName("Stark")
                .email("tony@ironman.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer( invocation -> invocation.getArgument(0));

        //when - action or the behaviour that we're going to test
        ResultActions rep = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        rep.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("Junit to get all employees")
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> list = new ArrayList<>();
        list.add(Employee.builder().firstName("Steve").lastName("Rogers").email("seteve@gmail.com").build());
        list.add(Employee.builder().firstName("Hawk").lastName("Eye").email("hawk@gmail.com").build());
        list.add(Employee.builder().firstName("Nat").lastName("Ben").email("nat@gmail.com").build());

        BDDMockito.given(employeeService.getAllEmployee()).willReturn(list);

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/allEmployees"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(list.size())));
    }

    @Test
    @DisplayName("Junit to find employee by id")
    public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Hawk").lastName("Eye").email("hawk@gmail.com").build();
        int id = 1;
        BDDMockito.given(employeeService.getEmployeeById(id)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", id));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())));
    }

    @Test
    @DisplayName("Junit to update employee - positive scenario")
    public void givenEmployeeId_whenUpdateEmployeeObject_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        int id = 1;
        Employee savedEmp = Employee.builder()
                .firstName("Chirs").lastName("Evans").email("chris@gmail.com").build();

        Employee updatedEmp = Employee.builder()
                .firstName("Chirs").lastName("Henry").email("chris.henry@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(id)).willReturn(Optional.of(savedEmp));
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is("chris.henry@gmail.com")));
    }

    @Test
    @DisplayName("Junit to update employee - negative scenario")
    public void givenEmployeeId_whenUpdateEmployeeObject_thenReturnNegativeResponse() throws Exception {
        //given - precondition or setup
        int id = 1;
        Employee savedEmp = Employee.builder()
                .firstName("Chirs").lastName("Evans").email("chris@gmail.com").build();

        Employee updatedEmp = Employee.builder()
                .firstName("Chirs").lastName("Henry").email("chris.henry@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Junit to delete employee")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessful() throws Exception {
        //given - precondition or setup
        int id = 1;
        BDDMockito.willDoNothing().given(employeeService).deleteById(id);

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", id));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
