package com.project.AzCar.Security.Handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Services.Users.UserServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnAuthenticationFailedHandler implements AuthenticationFailureHandler {

	 @Autowired
	    private UserServices userServices;

	    @Override
	    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                        AuthenticationException exception) throws IOException, ServletException {
	        String email = request.getParameter("email");
	        Users user = userServices.findUserByEmail(email);

	        if (user != null && user.isEnabled()) {
	            // User is blocked
	            request.getSession().setAttribute("signin_error", "Your account is blocked. Please contact support.");
	        } else {
	            // Wrong email or password
	            request.getSession().setAttribute("signin_error", "Wrong email or password!");
	        }

	        response.sendRedirect("/login");
	    }

}