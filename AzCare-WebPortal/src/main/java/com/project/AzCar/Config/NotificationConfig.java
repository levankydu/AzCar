package com.project.AzCar.Config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class NotificationConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests.requestMatchers("/", "/ws/**").permitAll())
				.authorizeHttpRequests(requests -> requests.anyRequest().authenticated()).formLogin(withDefaults())
				.logout(logout -> logout.logoutSuccessUrl("/"));
		return http.build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("test").password("test").roles("USER").build();

		return new InMemoryUserDetailsManager(user);
	}
}
