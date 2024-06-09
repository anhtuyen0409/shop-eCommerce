package com.nguyenanhtuyen.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.exception.UserNotFoundException;
import com.nguyenanhtuyen.admin.repository.RoleRepository;
import com.nguyenanhtuyen.admin.repository.UserRepository;
import com.nguyenanhtuyen.common.entity.Role;
import com.nguyenanhtuyen.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}
	
	public void save(User user) {
		boolean isUpdatingUser = (user.getId() != null); // user is existing -> update
		if(isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		userRepository.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User user = userRepository.getUserByEmail(email);
		if(user == null) {
			return true;
		}
		boolean isCreatingNew = (id == null);
		if(isCreatingNew) {
			if(user != null) {
				return false;
			}
		} else {
			if(user.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (Exception e) {
			throw new UserNotFoundException("Could not find user with id = " + id);
		}
	}
	
	public void deleteUser(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find user with id: " + id);
		}
		userRepository.deleteById(id);
	}
}
