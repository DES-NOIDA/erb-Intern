package com.thales.ERB.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thales.ERB.ErbApplication;

@Controller
public class MyErrorController implements ErrorController {

	private static final Logger logger = LogManager.getLogger(MyErrorController.class);

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		try {
			logger.debug("MyErrorController :: handleError :: start");
			Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			if (status != null) {
				Integer statusCode = Integer.valueOf(status.toString());
				if (statusCode == HttpStatus.NOT_FOUND.value()) {
					return "404error";
				} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
					return "500error";
				}
			}
			logger.debug("MyErrorController :: handleError :: end");
			return "error";
		} catch (Exception e) {
			logger.debug("MyErrorController :: handleError :: error occurred" + e.toString());
			return "400Error";
		}
	}
}
