package com.nguyenanhtuyen.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.repository.ProductPagingRepository;
import com.nguyenanhtuyen.admin.repository.ProductRepository;
import com.nguyenanhtuyen.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductPagingRepository productPagingRepository;
	
	public List<Product> listAll() {
		return (List<Product>) productRepository.findAll();
	}
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdatedTime(new Date());
		
		return productRepository.save(product);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreating = (id == null || id == 0);
		Product product = productPagingRepository.findByName(name);
		
		if(isCreating) {
			if(product != null) {
				return "Duplicate";
			}
		} else {
			if(product != null && product.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
}
