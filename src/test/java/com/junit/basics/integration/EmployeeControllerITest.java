package com.junit.basics.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.basics.model.Employee;
import com.junit.basics.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Junit to create employee")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Tony")
                .lastName("Stark")
                .email("tony@ironman.com")
                .build();

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
        employeeRepository.saveAll(list);

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
        Employee employee = Employee.builder()
                .firstName("Hawk").lastName("Eye").email("hawk@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())));
    }

    @Test
    @DisplayName("Junit to find employee by id - negative scenario")
    public void givenEmployeeId_whenFindEmployeeById_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tony").lastName("Stark").email("tony@gmail.com").build();
        employeeRepository.save(employee);
        int id = 4;

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employees/{id}", id));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Junit to update employee - positive scenario")
    public void givenEmployeeId_whenUpdateEmployeeObject_thenReturnUpdatedEmployee() throws Exception {
        //given - precondition or setup
        Employee savedEmp = Employee.builder()
                .firstName("Chirs").lastName("Evans").email("chris@gmail.com").build();
        employeeRepository.save(savedEmp);

        Employee updatedEmp = Employee.builder()
                .firstName("Chirs").lastName("Henry").email("chris.henry@gmail.com").build();

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/employees/{id}", savedEmp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is("chris.henry@gmail.com")));
    }

    @Test
    @DisplayName("Junit to delete employee")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnSuccessful() throws Exception {
        //given - precondition or setup
        Employee savedEmp = Employee.builder()
                .firstName("Chirs").lastName("Evans").email("chris@gmail.com").build();
        employeeRepository.save(savedEmp);

        //when - action or the behaviour that we're going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/employees/{id}", savedEmp.getId()));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
