package com.nguyenanhtuyen.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.nguyenanhtuyen.common.entity.Product;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Integer> {

	public Product findByName(String name);
	
}
