package com.nguyenanhtuyen.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository brandRepository;
	
	@Test
	public void testCreateBrand() {
		Category laptops = new Category(4);
		Brand dell = new Brand("Dell");
		dell.getCategories().add(laptops);
		
		Brand savedBrand = brandRepository.save(dell);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category smartphones = new Category(7);
		Category tablets = new Category(30);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(smartphones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = brandRepository.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {
		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(new Category(8));
		samsung.getCategories().add(new Category(21));
		
		Brand savedBrand = brandRepository.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = brandRepository.findAll();
		brands.forEach(System.out::println);
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = brandRepository.findById(1).get();
		assertThat(brand.getName()).isEqualTo("Dell");
	}
	
	@Test
	public void testUpdateBrand() {
		String newName = "test";
		Brand dell = brandRepository.findById(1).get();
		dell.setName(newName);
		
		Brand savedBrand = brandRepository.save(dell);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDeleteBrand() {
		Integer id = 3;
		brandRepository.deleteById(id);
		Optional<Brand> result = brandRepository.findById(id);
		assertThat(result.isEmpty());
	}
}
