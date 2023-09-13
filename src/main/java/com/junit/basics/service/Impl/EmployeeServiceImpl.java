package com.junit.basics.service.Impl;

import com.junit.basics.exceptions.ResourceNotFoundException;
import com.junit.basics.model.Employee;
import com.junit.basics.repository.EmployeeRepository;
import com.junit.basics.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> emp = employeeRepository.findByEmail(employee.getEmail());
        if(emp.isPresent())
            throw new ResourceNotFoundException("Employee already exists with given email "+emp.get().getEmail());
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> allEmp = employeeRepository.findAll();
        return allEmp;
    }

    @Override
    public Optional<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

}
