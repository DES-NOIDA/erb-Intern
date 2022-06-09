package com.thales.ERB.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.service.EmployeeService;
import com.thales.ERB.service.ReferralService;

@Controller
@SessionScope
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ReferralService referralService;

	private static final Logger logger = LogManager.getLogger(EmployeeController.class);
	
	// handle method to handle view employee list
	@GetMapping("/employee/list")
	public String listEmployees(Model m) {
		try {
			logger.debug("EmployeeController :: listEmployees :: start");
			m.addAttribute("employee", employeeService.getAllEmployees());
			logger.debug("EmployeeController :: listEmployees :: end");
			return "employee";
		} catch (Exception e) {
			logger.debug("EmployeeController :: listEmployees :: error occurred" + e.toString());
			return "400Error";
		}
	}

	// handler method to save new employee request
	@GetMapping("/employee/new")
	public String createEmployeeForm(Model model) {
		try {
			logger.debug("EmployeeController :: createEmployeeForm :: start");
			// create employee object to hold employee data
			Employee employee = new Employee();
			model.addAttribute("employee", employee);
			logger.debug("EmployeeController :: createEmployeeForm :: end");
			return "add_employee";
		} catch (Exception e) {
			logger.debug("EmployeeController :: createEmployeeForm :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/employee/list")
	public String saveEmployee(@ModelAttribute("employee") Employee employee, Model model, RedirectAttributes redirectAttributes, HttpServletRequest req) {
		logger.debug("EmployeeController :: saveEmployee :: start");
		User user = (User) req.getSession().getAttribute("user");
		Employee emp = employeeService.saveEmployee(employee, user);
		if (null != emp) {
			redirectAttributes.addFlashAttribute("savedemployee", " added to employee list");
			redirectAttributes.addFlashAttribute("newemployee", employee);
		} else {
			redirectAttributes.addFlashAttribute("savedemployee", " Error occured while saving Employee, please check log for more details");
			employee.setEmployeeName("");
			redirectAttributes.addFlashAttribute("newemployee", employee);
		}
		logger.debug("EmployeeController :: saveEmployee :: end");
		return "redirect:/employee/list";
	}

	// Handler method to handle edit employee request
	@GetMapping("/employee/edit/{employeeId}")
	public String editEmployee(@PathVariable Long employeeId, Model m) {
		try {
			logger.debug("EmployeeController :: editEmployee :: start");
			m.addAttribute("employee", employeeService.getEmployeeById(employeeId));
			logger.debug("EmployeeController :: editEmployee :: end");
			return "edit_employee";
		}
		catch (Exception e) {
			logger.debug("EmployeeController :: editEmployee :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/employee/{employeeId}")
	public String updateEmployee(@PathVariable Long employeeId, @ModelAttribute("employee") Employee employee, Model m, RedirectAttributes redirectAttributes, HttpServletRequest req) {
		// get employee from db by id
		logger.debug("EmployeeController :: updateEmployee :: start");
		Employee existingEmployee = employeeService.getEmployeeById(employeeId);
		existingEmployee.setEmployeeName(employee.getEmployeeName());
		existingEmployee.setEmployeeTGI(employee.getEmployeeTGI());
		// save updated employee item
		User user = (User) req.getSession().getAttribute("user");
		Employee emp = employeeService.updateEmployee(existingEmployee, user);
		if (null != emp) {
			redirectAttributes.addFlashAttribute("updatedemployee", " details updated sucessfully");
			redirectAttributes.addFlashAttribute("editedemployee", employee);
		} else {
			redirectAttributes.addFlashAttribute("updatedemployee", " Error occured while updating Employee, please check log for more details");
			employee.setEmployeeName("");
			redirectAttributes.addFlashAttribute("editedemployee", employee);

		}
		logger.debug("EmployeeController :: updateEmployee :: end");
		return "redirect:/employee/list";
	}

	// Handler method to handle delete employee request
	@GetMapping("/employee/{employeeId}")
	public String deleteEmployee(@PathVariable Long employeeId, HttpServletRequest req) {
		try {
			logger.debug("EmployeeController :: deleteEmployee :: start");
			User user = (User) req.getSession().getAttribute("user");
			employeeService.DeleteEmployeebyId(employeeId, user);
			logger.debug("EmployeeController :: deleteEmployee :: end");
			return "redirect:/employee/list";
		}
		catch (Exception e) {
			logger.debug("EmployeeController :: deleteEmployee :: error occurred" + e.toString());
			return "400Error";
		}
	}

	// Handler method to show dashboard request
	@GetMapping("/dashboard")
	public String viewDashboard(@RequestParam(required = false) String year, Model m) {
		try {
			logger.debug("EmployeeController :: viewDashboard :: start");
			int currentyear;
			if(year == null) {
				currentyear=LocalDate.now().getYear();
			}
			else {
				currentyear = Integer.parseInt(year) ;
			}
			List<Referral> referral = referralService.getReferralsByYear(currentyear);
			Set<Employee> employeeSet = new HashSet<>();
			int probationYes = 0, probationNo = 0, onboardNo = 0;
			for (Referral ref : referral) {
				employeeSet.add(ref.getEmployee());
				if (ref.getOnboarded().equalsIgnoreCase("No"))
					onboardNo++;
				else if (ref.getProbation().equalsIgnoreCase("No"))
					probationNo++;
				else if (ref.getProbation().equalsIgnoreCase("Yes"))
					probationYes++;
			}
			List<Employee> employee = new ArrayList<>(employeeSet);
			Collections.sort(employee);
			List<Employee> winners = new LinkedList<Employee>();
			List<Employee> consolation = new LinkedList<Employee>();
			int size = employee.size();
			if (size <= 5) {
				winners.addAll(employee);
			} else if (size <= 10) {
	
				for (int i = 0; i < 5; i++) {
					winners.add(employee.get(i));
				}
				for (int i = 5; i < size; i++) {
					consolation.add(employee.get(i));
				}
			} else {
				for (int i = 0; i < 5; i++) {
					winners.add(employee.get(i));
				}
				for (int i = 5; i < 10; i++) {
					consolation.add(employee.get(i));
				}
			}
			m.addAttribute("referrals", referral.size());
			m.addAttribute("onboardNo", onboardNo);
			m.addAttribute("probationNo", probationNo);
			m.addAttribute("probationYes", probationYes);
			m.addAttribute("winners", winners);
			m.addAttribute("consolation", consolation);
			logger.debug("EmployeeController :: viewDashboard :: end");
			return "dashboard";
		}
		catch (Exception e) {
			logger.debug("EmployeeController :: viewDashboard :: error occurred" + e.toString());
			e.printStackTrace();
			return "400Error";
		}
	}

	
	// handler method to view user detail page
	@GetMapping("/employee/view-referrals/{employeeId}")
	public String viewReferrals(@PathVariable Long employeeId, Model m) {
		try {
			logger.debug("EmployeeController :: viewReferrals :: start");
			Employee employee = employeeService.getEmployeeById(employeeId);
			m.addAttribute("referral", employee.getReferral());
			m.addAttribute("employee", employee);
			logger.debug("EmployeeController :: viewReferrals :: end");
			return "user_detail";
		}
		catch (Exception e) {
			logger.debug("EmployeeController :: viewReferrals :: error occurred" + e.toString());
			return "400Error";
		}
	}

}
