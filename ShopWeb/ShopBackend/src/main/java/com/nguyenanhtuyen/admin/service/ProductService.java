package com.nguyenanhtuyen.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.repository.ProductRepository;
import com.nguyenanhtuyen.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> listAll() {
		return productRepository.findAll();
	}
}
