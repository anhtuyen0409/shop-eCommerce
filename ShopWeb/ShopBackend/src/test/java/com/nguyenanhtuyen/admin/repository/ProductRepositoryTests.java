package com.nguyenanhtuyen.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Category;
import com.nguyenanhtuyen.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand= testEntityManager.find(Brand.class, 1);
		Category category = testEntityManager.find(Category.class, 4);
		
		Product product = new Product();
		product.setName("Dell Inspiron 15 3520 i5 1235U");
		product.setAlias("dell_inspiron_15_3520_i5_1235U");
		product.setShortDescription("This is the short description for laptop from dell");
		product.setFullDescription("this is the full description for laptop from dell");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(16500000);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = productRepository.findAll();
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Product	product = productRepository.findById(1).get();
		System.out.println(product);
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = productRepository.findById(id).get();
		product.setPrice(15000000);
		productRepository.save(product);
		
		Product updatedProduct = testEntityManager.find(Product.class, id);
		
		assertThat(updatedProduct.getPrice()).isEqualTo(15000000);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 2;
		productRepository.deleteById(id);
		
		Optional<Product> result = productRepository.findById(id);
		
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = productRepository.findById(productId).get();
		
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image-1.png");
		product.addExtraImage("extra image-2.png");
		product.addExtraImage("extra image-3.png");
		
		Product savedProduct = productRepository.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
}
