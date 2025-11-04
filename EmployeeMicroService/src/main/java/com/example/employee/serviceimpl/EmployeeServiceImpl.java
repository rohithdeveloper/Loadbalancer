package com.example.employee.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.employee.exceptioncontroller.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.employee.dto.AddressDto;
import com.example.employee.dto.EmployeeDto;
import com.example.employee.model.Address;
import com.example.employee.model.Employee;
import com.example.employee.modelmapper.UserMapper;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import org.springframework.cache.annotation.Cacheable;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private RestTemplate restTemplate;

    private static final String EMPLOYEE_SERVICE = "employeeService";

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee: {}", employeeDto.getFirstName() + " " + employeeDto.getLastName());
        Employee employee = UserMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = empRepo.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getEmpId());
        return UserMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> employees = empRepo.findAll();
        List<EmployeeDto> employeesDto = UserMapper.mapToEmployeeDto(employees);
        log.info("Retrieved {} employees", employeesDto.size());
        return employeesDto;
    }

    @Override
    @CircuitBreaker(name = EMPLOYEE_SERVICE, fallbackMethod = "getEmployeeByIdFallback")
    @Cacheable(value = "employee", key = "#id")
    public EmployeeDto getEmployeeById(long id) throws Exception {
        log.info("Fetching employee with ID: {}", id);
        Optional<Employee> employee = empRepo.findById(id);
        if (employee.isPresent()) {
            EmployeeDto empDto = UserMapper.mapToEmployeeDto(employee.get());
            log.info("Employee found: {} {}", empDto.getFirstName(), empDto.getLastName());

            // Fetch addresses from Address Microservice using Eureka service discovery
            // getForEntity(): Returns a ResponseEntity object containing the response body,
            // HTTP status code, headers, and other metadata
                ResponseEntity<AddressDto[]> responseEntity = restTemplate
                        .getForEntity("http://Address-MicroService/api/address/employee/" + empDto.getEmpId(), AddressDto[].class);
                AddressDto[] addressArray = responseEntity.getBody();
                HttpStatusCode status = responseEntity.getStatusCode();
                log.info("Address service response status: {}", status);
                
                // Handle null response body safely
                ArrayList<AddressDto> address = new ArrayList<>();
                if (addressArray != null) {
                    address = new ArrayList<>(Arrays.asList(addressArray));
                    log.info("Retrieved {} addresses for employee {}", address.size(), empDto.getEmpId());
                } else {
                    log.warn("No addresses found for employee {}", empDto.getEmpId());
                }
                
                empDto.setAddresses(address);
                return empDto;

        } else {
            log.warn("Employee not found with ID: {}", id);
            throw new Exception("Employee not found with ID: " + id);
        }

    }


    public EmployeeDto getEmployeeByIdFallback(long id, Throwable ex) {
        log.error("⚠️ Fallback triggered for employee {} due to: {}", id, ex.toString(), ex);
        throw new ServiceUnavailableException("Server problem, please try after sometime");
    }


}
