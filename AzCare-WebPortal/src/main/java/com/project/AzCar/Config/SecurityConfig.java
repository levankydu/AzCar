package com.project.AzCar.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.project.AzCar.Security.Handler.OnAuthenticationFailedHandler;
import com.project.AzCar.Security.Handler.OnAuthenticationSuccessHandler;
import com.project.AzCar.Services.Users.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.project.AzCar.*")
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> requests.requestMatchers("/").permitAll()
				.requestMatchers("/registeradmin").permitAll().requestMatchers("/register/**").permitAll()
				.requestMatchers("/login/**").permitAll().requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/dashboard/**").hasAnyRole("ADMIN").anyRequest().authenticated())
				.formLogin((form) -> form.loginPage("/login").usernameParameter("email").passwordParameter("password")
						.loginProcessingUrl("/dologin").successHandler(new OnAuthenticationSuccessHandler())
						.failureHandler(new OnAuthenticationFailedHandler()).permitAll())
				.logout((logout) -> logout.permitAll().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
				.exceptionHandling(handling -> handling.accessDeniedPage("/access-denied"));
		http.authenticationProvider(daoAuthen());
		return http.build();

	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider daoAuthen() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;

	}
}