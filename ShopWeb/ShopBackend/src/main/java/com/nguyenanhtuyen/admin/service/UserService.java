package com.nguyenanhtuyen.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.repository.UserRepository;
import com.nguyenanhtuyen.common.entity.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}
}
