package com.junit.basics.service;

import com.junit.basics.exceptions.ResourceNotFoundException;
import com.junit.basics.model.Employee;
import com.junit.basics.repository.EmployeeRepository;
import com.junit.basics.service.Impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee, emp1, emp2;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(110).firstName("Alex").lastName("Carey").email("alex@gmail.com").build();
        emp1= Employee.builder()
                .id(111).firstName("Steve").lastName("Smith").email("smith@gmail.com").build();
        emp2 = Employee.builder()
                .id(112).firstName("David").lastName("Warner").email("dwarner@gmail.com").build();
    }

    @Test
    @DisplayName("Junit for save employee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we're going to test
        Employee savedEmp = employeeService.saveEmployee(employee);

        //then - verify the output
        Assertions.assertThat(savedEmp).isNotNull();
    }

    @Test
    @DisplayName("Junit for save employee method which throw exception")
    public void givenEmployeeObject_whenSaveEmployee_thenThrowException(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we're going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Junit to get all employee")
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of(employee, emp1, emp2));

        //when - action or the behaviour that we're going to test
        List<Employee> list = employeeService.getAllEmployee();

        //then - verify the output
        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Junit to get all employee - negative scenario")
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeeList(){
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we're going to test
        List<Employee> list = employeeService.getAllEmployee();

        //then - verify the output
        Assertions.assertThat(list).isEmpty();
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Junit find employee by Id")
    public void givenEmployeeId_whenFindById_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(111)).willReturn(Optional.of(emp1));

        //when - action or the behaviour that we're going to test
        Employee emp = employeeService.getEmployeeById(emp1.getId()).get();

        //then - verify the output
        Assertions.assertThat(emp).isNotNull();
        Assertions.assertThat(emp.getFirstName()).isEqualTo("Steve");
    }

    @Test
    @DisplayName("Junit for update employee")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setLastName("Ferguson");

        //when - action or the behaviour that we're going to test
        Employee updatedEmp = employeeService.saveEmployee(employee);

        //then - verify the output
        Assertions.assertThat(updatedEmp).isNotNull();
        Assertions.assertThat(updatedEmp.getLastName()).isEqualTo("Ferguson");
    }

    @Test
    @DisplayName("Junit to delete employee")
    public void givenEmployeeId_whenDeleteById_thenReturnNothing(){
        //given - precondition or setup
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(112);

        //when - action or the behaviour that we're going to test
        employeeService.deleteById(112);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(112);
    }
}
