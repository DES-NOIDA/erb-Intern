package com.thales.ERB.repo.impl;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.repo.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LogManager.getLogger(EmployeeRepositoryImpl.class);
	
	@Override
	public List<Employee> findAll() {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("EmployeeRepositoryImpl :: findAll  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			List<Employee> employeelist = session.createQuery("from Employee").list();
			txn.commit();
			logger.debug("EmployeeRepositoryImpl :: findAll  :: end");
			return employeelist;
		}
		catch(Exception e) {
			logger.debug("EmployeeRepositoryImpl :: findAll  :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Employee findById(Long employeeId) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("EmployeeRepositoryImpl :: findById  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, employeeId);
			txn.commit();
			logger.debug("EmployeeRepositoryImpl :: findById  :: end");
			return employee;
		}
		catch(Exception e) {
			logger.debug("EmployeeRepositoryImpl :: findById  :: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Employee save(Employee employee, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("EmployeeRepositoryImpl :: save  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			employee.setNumberOfReferrals(0);
			employee.setTotalPoints(0l);
			employee.setOnboardPoints(0l);
			employee.setExtraPoints(0l);
			session.saveOrUpdate(employee);
			logger.info(user.getUsername() + " added employee " + employee.getEmployeeName() + " (" + employee.getEmployeeTGI() + ")");
			txn.commit();
			logger.debug("EmployeeRepositoryImpl :: save  :: end");
			return employee;
		}
		catch(Exception e) {
			logger.debug("EmployeeRepositoryImpl :: save  :: error occurred " + e.toString());
			txn.rollback();
			employee = null;
			return employee;
		}
		finally {
			session.close();
		}
	}

	@Override
	public Employee update(Employee employee, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("EmployeeRepositoryImpl ::  update  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			session.saveOrUpdate(employee);
			logger.info(user.getUsername() + " updates employee " + employee.getEmployeeName() + " (" + employee.getEmployeeTGI() + ")");
			txn.commit();
			session.close();
			logger.debug("EmployeeRepositoryImpl :: update  :: end");
			return employee;
		}
		catch(Exception e) {
			logger.debug("EmployeeRepositoryImpl :: update  ::: error occurred " + e.toString());
			txn.rollback();
			return null;
		}
		finally {
			session.close();
		}
	}
	
	@Override
	public void deleteById(Long employeeId, User user) {
		Session session = null;
		Transaction txn = null;
		try {
			logger.debug("EmployeeRepositoryImpl :: deleteById  :: start");
			session = this.sessionFactory.openSession();
			txn = session.beginTransaction();
			Employee emp = (Employee) session.load(Employee.class, new Long(employeeId));
			if (null != emp) {
				session.delete(emp);
				logger.info(user.getUsername() + " deleted employee " + emp.getEmployeeName() + " (" + emp.getEmployeeTGI() + ")");
			}
			txn.commit();
			session.close();
			logger.debug("EmployeeRepositoryImpl :: deleteById :: end");
		}
		catch(Exception e) {
			logger.debug("EmployeeRepositoryImpl :: deleteById  ::: error occurred" + e.toString());
			txn.rollback();
		}
		finally {
			session.close();
		}
		
	}

}
