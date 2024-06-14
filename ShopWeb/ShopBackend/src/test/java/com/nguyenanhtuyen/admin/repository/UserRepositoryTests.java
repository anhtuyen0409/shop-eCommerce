package com.nguyenanhtuyen.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.nguyenanhtuyen.common.entity.Role;
import com.nguyenanhtuyen.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserPagingRepository userPagingRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin = testEntityManager.find(Role.class, 1);
		User user = new User("tuyendev@gmail.com", "123", "Tuyen", "Nguyen Anh");
		user.addRole(roleAdmin);
		
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		User user = new User("teo@gmail.com", "123", "Teo", "Nguyen Van");
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
		
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = userRepository.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = userRepository.findById(1).get();
		user.setEnabled(true);
		user.setEmail("tuyenna@gmail.com");
		
		userRepository.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = userRepository.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSales = new Role(2);
		
		user.getRoles().remove(roleEditor);
		user.addRole(roleSales);
		
		userRepository.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		userRepository.deleteById(2);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "teo@gmail.com";
		User user = userRepository.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 100;
		Long countById = userRepository.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		userRepository.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userPagingRepository.findAll(pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
}
