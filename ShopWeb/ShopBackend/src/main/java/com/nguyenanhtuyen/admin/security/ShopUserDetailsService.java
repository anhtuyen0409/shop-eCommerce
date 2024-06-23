package com.nguyenanhtuyen.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.repository.UserRepository;
import com.nguyenanhtuyen.common.entity.User;

@Service
public class ShopUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.getUserByEmail(email);
		if(user != null) {
			return new ShopUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}

}
