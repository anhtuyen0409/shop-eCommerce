package com.nguyenanhtuyen.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.nguyenanhtuyen.common.entity.Brand;

public interface BrandPagingRepository extends PagingAndSortingRepository<Brand, Integer> {

	public Long countById(Integer id);
}
