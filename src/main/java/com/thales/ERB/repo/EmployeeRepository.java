package com.thales.ERB.repo;

import java.sql.SQLException;
import java.util.List;

import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.User;

public interface EmployeeRepository {

	public List<Employee> findAll();

	public Employee findById(Long employeeId);

	public Employee save(Employee employee, User user);
	
	public Employee update(Employee employee, User user);

	public void deleteById(Long employeeId, User user);

}