package com.nguyenanhtuyen.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.exception.UserNotFoundException;
import com.nguyenanhtuyen.admin.repository.RoleRepository;
import com.nguyenanhtuyen.admin.repository.UserPagingRepository;
import com.nguyenanhtuyen.admin.repository.UserRepository;
import com.nguyenanhtuyen.common.entity.Role;
import com.nguyenanhtuyen.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	
	public static final int USERS_PER_PAGE = 10;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserPagingRepository userPagingRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		return userPagingRepository.findAll(pageable);
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}
	
	public User save(User user) {
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
		return userRepository.save(user);
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
