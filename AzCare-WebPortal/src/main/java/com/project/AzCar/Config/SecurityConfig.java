package com.project.AzCar.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//		http.authorizeHttpRequests((authorize) -> 
//				authorize
//				.requestMatchers("/dashboard", "/users/**", "/roles/**","/")
////				.hasAnyAuthority("ADMIN")
////				.requestMatchers("/login", "/forgot_password", "/apis/test/**", "/apis/v1/login","/")
//				.permitAll()
//				.anyRequest()
//				.authenticated());
		
		http
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/login", "/register", "/roles/**","/")
				.permitAll()
				.anyRequest()
				.authenticated()
				
				
			)
		;

		
//		http.formLogin((login) -> 
//				login
//				.permitAll()
//				.loginPage("/login")
//				.usernameParameter("email")
//				.passwordParameter("password")
//				.loginProcessingUrl("/dologin")
//				.defaultSuccessUrl("/",false)
//				.failureForwardUrl("login_error")
//				);
	
//		http.logout((logout)->logout.permitAll());
		
		
//		http.authenticationProvider(daoAuthen());
		return http.build();

	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**");
	}
}
