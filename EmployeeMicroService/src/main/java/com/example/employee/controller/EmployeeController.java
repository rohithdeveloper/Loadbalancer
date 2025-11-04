package com.example.employee.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.example.employee.dto.EmployeeDto;
import com.example.employee.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployeeController {

    @Value("${server.port}")
    private String portNo;

    @Autowired
    private EmployeeService empService;

    @PostMapping("/employees")
    public EmployeeDto addUser(@RequestBody EmployeeDto empDto) {
        return empService.createEmployee(empDto);

    }

    @GetMapping("/employees/{id}")
    public EmployeeDto getUserById(@PathVariable("id") long empId) throws Exception {
        EmployeeDto empDto = empService.getEmployeeById(empId);
        return empDto;
    }

    @GetMapping("/employees")
    public List<EmployeeDto> getAllEmployees() {
        log.info("=== Employee Service running on port: {} ===", portNo);
        return empService.getAllEmployees();
    }

    @GetMapping("/allEmployes")
    public List<EmployeeDto> getAllEmployeesOld() {
        log.info("=== Employee Service running on port: {} ===", portNo);
        return empService.getAllEmployees();
    }
}

//for cache put use @CachPut(value,key)
//for cache put use @CachEvict(value,key)
//Types of Caching
//1. InMemory: Cache will be created indside memory of your application uses concurrentHashMap (Heap Memory)