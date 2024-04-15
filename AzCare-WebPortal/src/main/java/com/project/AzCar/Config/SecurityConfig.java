package com.project.AzCar.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import com.project.AzCar.Security.Handler.CustomOAuth2User;
import com.project.AzCar.Security.Handler.CustomOAuth2UserService;
import com.project.AzCar.Security.Handler.OnAuthenticationFailedHandler;
import com.project.AzCar.Security.Handler.OnAuthenticationSuccessHandler;
import com.project.AzCar.Services.Users.CustomUserDetailsService;
import com.project.AzCar.Services.Users.UserGoogleServices;
import com.project.AzCar.Utilities.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.project.AzCar.*")
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((requests) -> requests.requestMatchers("/").permitAll().requestMatchers("/ws/**")
						.permitAll().requestMatchers("/api/**").permitAll().requestMatchers("/data/**").permitAll()
						.requestMatchers("/api/cars/**").permitAll().requestMatchers("/user/profile/flutter/avatar/**")
						.permitAll().requestMatchers("/home/availablecars/flutter/img/**").permitAll()
						.requestMatchers("/forgot_password/**").permitAll().requestMatchers("/reset_password/**")
						.permitAll().requestMatchers("/register/**").permitAll().requestMatchers("/login/**")
						.permitAll().requestMatchers("/oauth2/**").permitAll().requestMatchers("/registeradmin")
						.permitAll().requestMatchers("/get/**").permitAll().requestMatchers("/home/carregister/**")
						.hasAnyRole("USER", "ADMIN").requestMatchers("/home/myplan/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/home/availablecars**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/home/availablecars/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/user/profile/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/comment/create").hasAnyRole("USER", "ADMIN").requestMatchers("/dashboard/**")
						.hasAnyRole("ADMIN").requestMatchers("/dashboard/carverify/**").hasAnyRole("ADMIN").anyRequest()
						.authenticated())

				.formLogin((form) -> form.loginPage("/login").usernameParameter("email").passwordParameter("password")
						.loginProcessingUrl("/dologin").successHandler(new OnAuthenticationSuccessHandler())
						.failureHandler(new OnAuthenticationFailedHandler()).permitAll())

				.oauth2Login((form) -> form.loginPage("/login")
						.userInfoEndpoint((info) -> info.userService(oauthUserService))
						.successHandler(new OnAuthenticationSuccessHandler() {
							@Override
							public void onAuthenticationSuccess(HttpServletRequest request,
									HttpServletResponse response, Authentication authentication)
									throws IOException, ServletException {
								// Kiểm tra xác thực thành công
								System.out.println("AuthenticationSuccessHandler invoked");
								System.out.println("Authentication name: " + authentication.getName());
								CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
								userServices.processOAuthPostLogin(oauthUser.getEmail());

								String name = authentication.getName();
								System.out.println("Success logged in user: " + name);

								if (authentication.getPrincipal() instanceof OAuth2User) {
									System.out.println("Success logged in user---------------: " + name);
									OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
									String email = oauth2User.getAttribute("email");
//									request.getSession().setAttribute("isAuthen", "a");
//									request.getSession().setAttribute("user", email);
//									request.getSession().setAttribute("role", "user");
									System.out.println("Google:" + email);
									response.sendRedirect("/login");
								}
								// Xác định lỗi và redirect tùy thuộc vào quyền truy cập
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
								}
							}
						}).permitAll())

				.logout((logout) -> logout.permitAll().logoutUrl("/logout").logoutSuccessUrl("/")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID"))
				// .exceptionHandling(handling -> handling.accessDeniedPage("/access-denied"));
				.exceptionHandling(t -> System.out.print(t.toString()));
		http.authenticationProvider(daoAuthen());

		return http.build();

	}

	@Bean
	CustomUserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthen());
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
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;

	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Autowired
	private CustomOAuth2UserService oauthUserService;

	@Autowired
	private UserGoogleServices userServices;

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