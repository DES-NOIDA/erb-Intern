package com.thales.ERB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		String requestURL = request.getRequestURL().toString();
		if (requestURL.contains("/login")) {
			return true;
		}
		if (requestURL.contains(".css") || requestURL.contains(".js") || requestURL.contains(".woff")) {
			return true;
		}
		if (null == request.getSession().getAttribute("auth")) {
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
			throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception exception) throws Exception {

	}
}
