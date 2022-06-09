package com.thales.ERB.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.User;
import com.thales.ERB.repo.EmployeeRepository;
import com.thales.ERB.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

	public EmployeeServiceImpl() {
		super();
	}

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> getAllEmployees() {
		try {
			logger.debug("EmployeeServiceImpl ::getAllEmployees  :: start");
			List<Employee> empList = employeeRepository.findAll();
			if(null != empList) {
				logger.debug("EmployeeServiceImpl :: getAllEmployees  :: end");
				return empList;
			}
			else {
				logger.debug("EmployeeServiceImpl :: getAllEmployees  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("EmployeeServiceImpl ::getAllEmployees  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Employee getEmployeeById(Long employeeId) {
		try {
			logger.debug("EmployeeServiceImpl :: getEmployeeById  :: start");
			Employee emp = employeeRepository.findById(employeeId);
			if(null != emp) {
				logger.debug("EmployeeServiceImpl :: getEmployeeById  :: end");
				return emp;
			}
			else {
				logger.debug("EmployeeServiceImpl :: getEmployeeById  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("EmployeeServiceImpl :: getEmployeeById  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Employee saveEmployee(Employee employee, User user) {
		try {
			logger.debug("EmployeeServiceImpl :: saveEmployee  :: start");
			Employee emp = employeeRepository.save(employee, user);
			if(null != emp) {
				logger.debug("EmployeeServiceImpl :: saveEmployee  :: end");
				return emp;
			}
			else {
				logger.debug("EmployeeServiceImpl :: saveEmployee  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("EmployeeServiceImpl :: saveEmployee  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public Employee updateEmployee(Employee employee, User user) {
		try {
			logger.debug("EmployeeServiceImpl :: updateEmployee  :: start");
			Employee emp = employeeRepository.update(employee, user);
			if(null != emp) {
				logger.debug("EmployeeServiceImpl :: updateEmployee  :: end");
				return emp;
			}
			else {
				logger.debug("EmployeeServiceImpl :: updateEmployee  :: end");
				return null;
			}
		} catch (Exception e) {
			logger.debug("EmployeeServiceImpl :: updateEmployee  :: error occurred" + e.toString());
			return null;
		}
	}

	@Override
	public void DeleteEmployeebyId(Long employeeId, User user) {
		try {
			logger.debug("EmployeeServiceImpl :: DeleteEmployeebyId  :: start");
			employeeRepository.deleteById(employeeId, user);
			logger.debug("EmployeeServiceImpl :: DeleteEmployeebyId  :: end");
		} catch (Exception e) {
			logger.debug("EmployeeServiceImpl :: DeleteEmployeebyId  :: error occurred" + e.toString());
		}
	}

}
