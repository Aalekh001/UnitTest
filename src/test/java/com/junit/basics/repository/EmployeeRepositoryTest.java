package com.junit.basics.repository;

import com.junit.basics.model.Employee;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setupEmployee(){
        employee = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();
    }
    @Test
    @DisplayName("Save Employee")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                                .firstName("Aalekh").lastName("Kumar")
                                .email("aalekh@gmail.com").build();

        //when - action or the behaviour that we're going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        // we have imported static method from Assertions class
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Employees List")
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();
        Employee employee2 = Employee.builder()
                .firstName("Ankit").lastName("Kumar")
                .email("ankit@gmail.com").build();
        Employee employee3 = Employee.builder()
                .firstName("Aditya").lastName("Kumar")
                .email("aditya@gmail.com").build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        //when - action or the behaviour that we're going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(3);
    }

    @DisplayName("Find By Id")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployee(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee emp = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(emp).isNotNull();
        Logger logger = LoggerFactory.getLogger(EmployeeRepositoryTest.class);
        logger.info(String.valueOf(emp.getId()));
    }

    @Test
    @DisplayName("Get Employee By Email")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee emp = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(emp).isNotNull();
        assertThat(emp.getFirstName()).isEqualTo("Aalekh");
    }

    @Test
    @DisplayName("Update Employee")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee emp = employeeRepository.findByEmail(employee.getEmail()).get();
        emp.setLastName("Jaiswal");
        emp.setEmail("aalekh.jaiswal@gmail.com");
        Employee updatedEmp = employeeRepository.save(emp);

        //then - verify the output
        assertThat(updatedEmp.getEmail()).isEqualTo("aalekh.jaiswal@gmail.com");
    }

    @Test
    @DisplayName("Employee Delete")
    public void givenEmployeeObject_whenDelete_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Aalekh").lastName("Kumar")
                .email("aalekh@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        employeeRepository.delete(employee);
        Optional<Employee> deletedEmp = employeeRepository.findByEmail("aalekh@gmail.com");

        //then - verify the output
        assertThat(deletedEmp).isEmpty();
    }

    @Test
    @DisplayName("Custom Query using JPQL using Query params")
    public void givenEmployeeFirstNameLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John").lastName("Cena")
                .email("john.cena@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee byJPQL = employeeRepository.findByJPQL("John", "Cena");

        //then - verify the output
        assertThat(byJPQL).isNotNull();
        assertThat(byJPQL.getEmail()).isEqualTo("john.cena@gmail.com");
    }

    @Test
    @DisplayName("Custom Query using JPQL using Named params")
    public void givenEmployeeFirstNameLastName_whenFindByJPQLNamed_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John").lastName("Cena")
                .email("john.cena@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee byJPQL = employeeRepository.findByJPQLNamedParams("John", "Cena");

        //then - verify the output
        assertThat(byJPQL).isNotNull();
        assertThat(byJPQL.getEmail()).isEqualTo("john.cena@gmail.com");
    }

    @Test
    @DisplayName("Native SQL query")
    public void givenFirstLastName_whenFindByNativeSql_thenReturnEmployeeObject(){
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John").lastName("Cena")
                .email("john.cena@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee savedEmp = employeeRepository.findByNativeSQL(employee.getFirstName(),
                                employee.getLastName());

        //then - verify the output
        assertThat(savedEmp).isNotNull();
        assertThat(savedEmp.getEmail()).isEqualTo("john.cena@gmail.com");
    }

    @Test
    @DisplayName("Native SQL query using Named params")
    public void givenFirstLastName_whenFindByNativeSqlNamed_thenReturnEmployeeObject(){
        //given - precondition or setup
        //making use of @BeforeEach method
        employeeRepository.save(employee);

        //when - action or the behaviour that we're going to test
        Employee savedEmp = employeeRepository.findByNativeSqlNamed(employee.getFirstName(),
                employee.getLastName());

        //then - verify the output
        assertThat(savedEmp).isNotNull();
        assertThat(savedEmp.getEmail()).isEqualTo("aalekh@gmail.com");
    }
}
