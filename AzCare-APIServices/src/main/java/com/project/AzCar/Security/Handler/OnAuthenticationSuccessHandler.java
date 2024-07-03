package com.project.AzCar.Security.Handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Users.UserRepository;
import com.project.AzCar.Services.Users.UserGoogleServices;
import com.project.AzCar.Services.Users.UserServices;
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

		if (authentication.getPrincipal() instanceof OAuth2User) {
			System.out.println("Oauth2USer-------------------------------------");
			OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
			String email = oauth2User.getAttribute("email");
			request.getSession().setAttribute("isAuthen", "a");
			request.getSession().setAttribute("user", email);
			request.getSession().setAttribute("role", "user");
			System.out.println("Google:" + email);
		}
		request.getSession().removeAttribute("signin_error");
		request.getSession().setAttribute("emailLogin", name);
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