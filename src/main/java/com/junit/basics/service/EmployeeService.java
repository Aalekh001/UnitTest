package com.junit.basics.service;

import com.junit.basics.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployee();

    Optional<Employee> getEmployeeById(int id);

    Employee updateEmployee(Employee employee);

    void deleteById(int id);
}
