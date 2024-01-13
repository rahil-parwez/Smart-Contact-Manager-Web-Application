package com.rahil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rahil.entities.ApplicationUser;
import com.rahil.repository.ApplicationUserRepository;

public class ApplicationUserServiceImpl implements UserDetailsService{

	@Autowired
	private ApplicationUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		ApplicationUser user = userRepository.getUserByUserName(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not found user! "+username);
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
