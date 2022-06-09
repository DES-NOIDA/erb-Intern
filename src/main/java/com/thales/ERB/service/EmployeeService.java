package com.thales.ERB.service;

import java.util.List;

import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.User;


public interface EmployeeService {

	List<Employee> getAllEmployees();
	
	Employee getEmployeeById(Long employeeId);

	Employee saveEmployee(Employee employee, User user);

	Employee updateEmployee(Employee employee, User user);

	void DeleteEmployeebyId(Long employeeId, User user);
	
}
