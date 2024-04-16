package com.project.AzCar.Services.Users;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Users.Provider;
import com.project.AzCar.Entities.Users.Roles;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Users.RoleRepository;
import com.project.AzCar.Repositories.Users.UserRepository;
import com.project.AzCar.Utilities.Constants;

import jakarta.transaction.Transactional;

@Service
public class UserGoogleServices {

	@Autowired
	private JavaMailSender mailSender;
    @Autowired
    private UserRepository repo;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public void processOAuthPostLogin(String email) {
        Users existUser = repo.findByEmail(email);
        
        Roles role = roleRepo.findByName(Constants.Roles.USER);
        if (role == null) {
            role = roleRepo.save(new Roles(Constants.Roles.USER));
        }

        
        if (existUser == null) {
           
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setProvider(Provider.GOOGLE);
            
            
            List<Roles> rolesList = new ArrayList<>();
            rolesList.add(role);
            newUser.setRoles(rolesList);
           
            try {

    			sendEmail(email);
    		} catch (Exception e) {

    			return ;
    		}
            String encodedPassword = passwordEncoder.encode("123");
            newUser.setPassword(encodedPassword);
            newUser.setEnabled(true);
            
            
            repo.save(newUser);
            
            System.out.println("Created new user: " + email);
        }
    }
    
    
    private void sendEmail(String email) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
		jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setFrom("AzCar@gmail.com", "AzCar");
		helper.setTo(email);

		String subject = "Welcom to AzCar";
		String content = "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi" + "<p>" + "Your password is 123." +"<p>" + "Please you need to change password.";
		
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
}