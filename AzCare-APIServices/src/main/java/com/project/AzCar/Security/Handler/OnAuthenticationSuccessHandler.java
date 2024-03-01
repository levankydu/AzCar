package com.project.AzCar.Security.Handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.project.AzCar.Utilities.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String name = authentication.getName();
		System.out.println("Success logged in user: " + name);

		request.getSession().removeAttribute("signin_error");
//		if (authentication.getAuthorities().toString().contains(Constants.Roles.USER)) {
//			request.getSession().setAttribute("user", name);
//			request.getSession().setAttribute("role", "user");
//			response.sendRedirect("/");
//		} else {
//			request.getSession().setAttribute("admin", name);
//			response.sendRedirect("dashboard/");
//		}
		if (authentication.getAuthorities().toString().contains(Constants.Roles.USER)) {
			// User role
			request.getSession().setAttribute("isAuthen", "a");
			request.getSession().setAttribute("user", name);
			request.getSession().setAttribute("role", "user");
			response.sendRedirect("/");
		} else if (authentication.getAuthorities().toString().contains(Constants.Roles.ADMIN)) {
			// Admin role
			request.getSession().setAttribute("isAuthen", "b");
			request.getSession().setAttribute("admin", name);
			response.sendRedirect("/dashboard/");
		} else {
			response.sendRedirect("/");
		}

	}
}