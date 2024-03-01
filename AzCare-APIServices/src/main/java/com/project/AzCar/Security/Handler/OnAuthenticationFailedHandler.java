package com.project.AzCar.Security.Handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnAuthenticationFailedHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String name = request.getParameter("email");
//		String password = request.getParameter("password");

		request.getSession().setAttribute("signin_error", "Wrong email or password!");

		// response.sendError(HttpServletResponse.SC_FORBIDDEN, "Wrong email or
		// password!");
		// response.sendRedirect("/login?error=" + Base64Helper.encodeUrlValue("Wrong
		// email or password!"));
		response.sendRedirect("/login");
	}

}