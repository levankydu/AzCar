package com.project.AzCar.Services.Users;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Users.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private Users user;
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users user = userRepo.findByEmail(email);
        if (user != null) {
        	
            return new org.springframework.security.core.userdetails.User(user.getEmail()
                    , user.getPassword(),
                    user.getRoles().stream()
                            .map((role) -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList()));
            
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}

}
