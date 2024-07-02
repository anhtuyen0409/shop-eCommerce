package com.nguyenanhtuyen.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nguyenanhtuyen.common.entity.Category;

public interface CategoryPagingRepository extends PagingAndSortingRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
}
