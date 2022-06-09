package com.thales.ERB.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thales.ERB.ErbApplication;
import com.thales.ERB.Excel.ReferralDataExcelExport;
import com.thales.ERB.entity.Constants;
import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.Referral;
import com.thales.ERB.entity.User;
import com.thales.ERB.service.EmployeeService;
import com.thales.ERB.service.ReferralService;

@Controller
@SessionScope
public class ReferralController {

	@Autowired
	private ReferralService referralService;

	@Autowired
	private EmployeeService employeeService;

	private static final Logger logger = LogManager.getLogger(ReferralController.class);
	
	@GetMapping("/referral/list")
	public String listReferrals(Model m) {
		try {
			logger.debug("ReferralController :: listReferrals :: start");
			m.addAttribute("referral", referralService.getAllReferrals());
			logger.debug("ReferralController :: listReferrals :: end");
			return "referral";
		} catch (Exception e) {
			logger.debug("ReferralController :: listReferrals :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@GetMapping("/referral/new")
	public String createReferralForm(Model model) {
		try {
			logger.debug("ReferralController :: createReferralForm :: start");
			// create referral object to hold referral data
			Referral referral = new Referral();
			List<Employee> list = employeeService.getAllEmployees();
			model.addAttribute("referral", referral);
			model.addAttribute("list", list);
			logger.debug("ReferralController :: createReferralForm :: end");
			return "add_referral";
		} catch (Exception e) {
			logger.debug("ReferralController :: createReferralForm :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/referral/list")
	public String saveReferral(@ModelAttribute("referral") Referral referral, RedirectAttributes redirectAttributes, HttpServletRequest req) {
		logger.debug("ReferralController :: saveReferral :: start");
		User user = (User) req.getSession().getAttribute("user");
		Referral ref = referralService.saveReferral(referral, user);
		if (null != ref) {
			redirectAttributes.addFlashAttribute("savedreferral", " added to referral list");
			redirectAttributes.addFlashAttribute("newreferral", referral);
		} else {
			redirectAttributes.addFlashAttribute("savedreferral", " Error occured while saving Referral, please check log for more details");
			referral.setReferralName("");
			redirectAttributes.addFlashAttribute("newreferral", referral);
		}
		logger.debug("ReferralController :: saveReferral :: end");
		return "redirect:/referral/list";

	}

	// Handler method to handle edit referral request
	@GetMapping("/referral/edit/{referralId}")
	public String editReferral(@PathVariable Long referralId, Model m) {
		try {
			logger.debug("ReferralController :: editReferral :: start");
			Referral referral = referralService.getReferralById(referralId);
			List<Employee> list = employeeService.getAllEmployees();
			m.addAttribute("list", list);
			m.addAttribute("referral", referral);
			logger.debug("ReferralController :: editReferral :: end");
			return "edit_referral";
		} catch (Exception e) {
			logger.debug("ReferralController :: editReferral :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/referral/{referralId}")
	public String updateReferral(@PathVariable Long referralId, @ModelAttribute("referral") Referral referral, Model m,
			RedirectAttributes redirectAttributes, HttpServletRequest req) {
		logger.debug("ReferralController :: updateReferral :: start");
		// get referral from db by id
		Referral existingReferral = referralService.getReferralById(referralId);
		if (!existingReferral.getReferralStatus().equalsIgnoreCase(Constants.STATUS_INACTIVE)) {
			existingReferral.setReferreeTGI(referral.getReferreeTGI());
			existingReferral.setReferralName(referral.getReferralName());
			existingReferral.setReferralTGI(referral.getReferralTGI());
			existingReferral.setDateOfReferral(referral.getDateOfReferral());
			existingReferral.setDateOfJoining(referral.getDateOfJoining());
			existingReferral.setDateOfProbation(referral.getDateOfProbation());
			existingReferral.setOffer(referral.getOffer());
			existingReferral.setProbation(referral.getProbation());
			existingReferral.setOnboarded(referral.getOnboarded());
			existingReferral.setPositionOffered(referral.getPositionOffered());
			existingReferral.setCandidateId(referral.getCandidateId());
			// save updated referral
			User user = (User) req.getSession().getAttribute("user");
			Referral ref = referralService.updateReferral(existingReferral, user);
			if (null != ref) {
				redirectAttributes.addFlashAttribute("updatedreferral", " details updated sucessfully");
				redirectAttributes.addFlashAttribute("editedreferral", referral);
			} else {
				redirectAttributes.addFlashAttribute("updatedreferral", " Error occured while updating Referral, please check log for more details");
				referral.setReferralName("");
				redirectAttributes.addFlashAttribute("editedreferral", referral);
			}
		}
		logger.debug("ReferralController ::updateReferral :: end");
		return "redirect:/referral/list";

	}

	// Handler method to handle delete referral request
	@GetMapping("/referral/{referralId}")
	public String deleteReferral(@PathVariable Long referralId, HttpServletRequest req) {
		try {
			logger.debug("ReferralController :: deleteReferral :: start");
			User user = (User) req.getSession().getAttribute("user");
			referralService.deleteReferralbyId(referralId, user);
			logger.debug("ReferralController :: deleteReferral :: end");
			return "redirect:/referral/list";
		}
		catch (Exception e) {
			logger.debug("ReferralController :: deleteReferral :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@GetMapping("/referral/excelExport")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		try {
			logger.debug("ReferralController :: exportToExcel :: start");
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			DateFormat dateFormattter = new SimpleDateFormat("yyyy-MM-dd");
			String currentDateTime = dateFormattter.format(new Date());
			String fileName = "referralList_" + currentDateTime + ".xlsx";
			String headerValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, headerValue);
			List<Referral> referrallist = referralService.getAllReferrals();
			ReferralDataExcelExport excelexporter = new ReferralDataExcelExport(referrallist);
			excelexporter.export(response);
			logger.debug("ReferralController :: exportToExcel :: end");
		}
		catch (Exception e) {
			logger.debug("ReferralController :: exportToExcel :: error occurred" + e.toString());
		}

	}

}
