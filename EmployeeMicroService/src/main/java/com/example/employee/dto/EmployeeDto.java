package com.example.employee.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private long empId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String branch;

	private List<AddressDto> addresses=new ArrayList<>();

	

}