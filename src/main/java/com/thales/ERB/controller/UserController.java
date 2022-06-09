package com.thales.ERB.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thales.ERB.entity.Employee;
import com.thales.ERB.entity.User;
import com.thales.ERB.service.UserService;

@Controller
@SessionScope
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	BCryptPasswordEncoder bcryptEncoder;

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@GetMapping("/login")
	public ModelAndView login() {
		logger.debug("UserController ::login  :: start");
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("user", new User());
		logger.debug("UserController :: login :: end");
		return mav;
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, HttpServletRequest req,
			RedirectAttributes redirectAttributes) {
		logger.debug("UserController :: login :: start");
		User retrivedUser = userService.getUserByName(user.getUsername());
		logger.debug("UserController :: RetrievedUser :: " + retrivedUser.getPassword());
		logger.debug("UserController :: Matches :: "
				+ bcryptEncoder.matches(user.getPassword(), retrivedUser.getPassword()));

		// Verify Encrypted password
		if (bcryptEncoder.matches(user.getPassword(), retrivedUser.getPassword())) {
			// User is authenticated now flow to be redirected on /dashboard
			req.getSession().setAttribute("user", retrivedUser);
			req.getSession().setAttribute("auth", "true");
			if (Objects.nonNull(retrivedUser)) {
				logger.debug("UserController :: login :: end");
				return "redirect:/dashboard";
			}
		} else {
			// User is not authenitc then redirect on login with error message "User name or
			// password is incorrect";
			logger.debug("UserController :: login :: errorinCredentials");
			redirectAttributes.addFlashAttribute("message", "Wrong TGI/Password");
			// boolean enableFlag = true;
			return "redirect:/login";
		}
		return null;
	}

	@RequestMapping(value = { "/logout" }, method = RequestMethod.POST)
	public String logoutDo(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		request.getSession().invalidate();
		redirectAttributes.addFlashAttribute("messagelogout", "You have been logged out !");
		logger.debug("UserController :: logoutDo :: loggedout");
		return "redirect:/login";
	}

	// handle method to handle view user list
	@GetMapping("/user/list")
	public String listUsers(Model m) {
		try {
			logger.debug("UserController :: listUsers :: start");
			m.addAttribute("user", userService.getAllUsers());
			logger.debug("UserController :: listUsers :: end");
			return "user";
		} catch (Exception e) {
			logger.debug("UserController :: listUser :: error occurred" + e.toString());
			return "400Error";
		}
	}

	// handler method to save new user request
	@GetMapping("/user/new")
	public String createUserForm(Model model) {
		try {
			logger.debug("UserController :: createUserForm :: start");
			// create user object to hold user data
			User user = new User();
			model.addAttribute("user", user);
			logger.debug("UserController :: createUserForm :: end");
			return "add_user";
		} catch (Exception e) {
			logger.debug("UserController :: createUserForm :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/user/list")
	public String saveUser(@ModelAttribute("user") User adminUser, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest req) {
		logger.debug("UserController :: saveUser :: start");
		User user = userService.saveUser(adminUser);
		if (null != user) {
			redirectAttributes.addFlashAttribute("saveduser", " added to user list");
			redirectAttributes.addFlashAttribute("newuser", user);
		} else {
			redirectAttributes.addFlashAttribute("saveduser", " Error occured while saving User, please check log for more details");
			redirectAttributes.addFlashAttribute("newuser", user);
		}
		logger.debug("UserController :: saveUser :: end");
		return "redirect:/dashboard";
	}

	// Handler method to handle edit user request
	@GetMapping("/user/edit/{username}")
	public String editUser(@PathVariable String username, Model m) {
		try {
			logger.debug("UserController :: editUser :: start");
			m.addAttribute("user", userService.getUserByName(username));
			logger.debug("UserController :: editUser :: end");
			return "edit_user";
		} catch (Exception e) {
			logger.debug("UserController :: editUser :: error occurred" + e.toString());
			return "400Error";
		}
	}

	@PostMapping("/user/{username}")
	public String updateUser(@PathVariable String username, @ModelAttribute("user") User adminUser, Model m,
			RedirectAttributes redirectAttributes, HttpServletRequest req) {
		// get user from db by id
		logger.debug("UserController :: updateUser :: start");
		User existingUser = userService.getUserByName(username);
		existingUser.setUsername(adminUser.getUsername());
		existingUser.setPassword(adminUser.getPassword());
		// save updated user item
		try {
			userService.updateUser(existingUser);
			
	} catch (Exception e) {
		logger.debug("UserController :: editUser :: error occurred" + e.toString());
		redirectAttributes.addFlashAttribute("updateduser", " Error occured while updating User, please check log for more details");
		return "400Error";
	}
		logger.debug("UserController :: updateUser :: end");
		return "redirect:/user/list";
	}

		// Handler method to handle delete user request
		@GetMapping("/user/{username}")
		public String deleteUser(@PathVariable String username, HttpServletRequest req) {
			try {
				logger.debug("UserController :: deleteUser :: start");
				userService.deleteUserByName(username);
				logger.debug("UserController :: deleteUser :: end");
				return "redirect:/user/list";
			}
			catch (Exception e) {
				logger.debug("UserController :: deleteUser :: error occurred" + e.toString());
				return "400Error";
			}
		}

}
