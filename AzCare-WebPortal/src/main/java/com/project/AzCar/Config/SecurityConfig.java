package com.project.AzCar.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((requests) -> requests.requestMatchers("/").permitAll().requestMatchers("/ws/**")
						.permitAll().requestMatchers("/api/**").permitAll().requestMatchers("/data/**").permitAll()
						.requestMatchers("/api/cars/**").permitAll().requestMatchers("/user/profile/flutter/avatar/**")
						.permitAll().requestMatchers("/home/availablecars/flutter/img/**").permitAll()
						.requestMatchers("/forgot_password/**").permitAll().requestMatchers("/reset_password/**")
						.permitAll().requestMatchers("/register/**").permitAll().requestMatchers("/login/**")
						.permitAll().requestMatchers("/registeradmin").permitAll().requestMatchers("/get/**")
						.permitAll().requestMatchers("/home/carregister/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/home/myplan/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/home/availablecars**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/home/availablecars/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/user/profile/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/comment/create").hasAnyRole("USER", "ADMIN").requestMatchers("/dashboard/**")
						.hasAnyRole("ADMIN").requestMatchers("/dashboard/carverify/**").hasAnyRole("ADMIN").anyRequest()
						.authenticated())
				.formLogin((form) -> form.loginPage("/login").usernameParameter("email").passwordParameter("password")
						.loginProcessingUrl("/dologin").successHandler(new OnAuthenticationSuccessHandler())
						.failureHandler(new OnAuthenticationFailedHandler()).permitAll())
				.logout((logout) -> logout.permitAll().logoutUrl("/logout").logoutSuccessUrl("/")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
				.exceptionHandling(handling -> handling.accessDeniedPage("/access-denied"));
		http.authenticationProvider(daoAuthen());
		return http.build();

	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**").requestMatchers("/adminAssets/**");
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

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

//	@Bean
//	Firestore getFireStore() throws IOException {
//
//		GoogleCredentials credentials = retrieveCredentials();
//		System.out.println(credentials);
//		if (credentials == null) {
//			System.out.println("Failed to create GoogleCredentials!");
//			return null;
//		}
//
//		FirebaseOptions options = new FirebaseOptions.Builder()
//				.setCredentials(credentials)
//				.build();
//
//		try {
//			FirebaseApp.initializeApp(options);
//		} catch (Exception e) {
//			System.err.println("Error initializing Firebase App: " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//
//		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
//				.setProjectId("azcar3-39149").build();
//
//		return firestoreOptions.getService();
//	}
//
//	private GoogleCredentials retrieveCredentials() throws IOException {
//		FileInputStream serviceAccount = new FileInputStream("src/main/resources/keyFirebase/azcar3.json");
//
//		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//		System.out.println(credentials);
//		if (credentials == null) {
//			System.out.println("Failed to create GoogleCredentials!");
//			return null;
//		}
//		return credentials;
//	}

}