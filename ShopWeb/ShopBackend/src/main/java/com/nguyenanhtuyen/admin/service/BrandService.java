package com.nguyenanhtuyen.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.exception.BrandNotFoundException;
import com.nguyenanhtuyen.admin.repository.BrandPagingRepository;
import com.nguyenanhtuyen.admin.repository.BrandRepository;
import com.nguyenanhtuyen.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private BrandPagingRepository brandPagingRepository;
	
	public List<Brand> listAll() {
		return (List<Brand>) brandRepository.findAll();
	}
	
	public Page<Brand> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNumber - 1, BRANDS_PER_PAGE, sort);
		
		if(keyword != null) {
			return brandPagingRepository.findAll(keyword, pageable);
		}
		
		return brandPagingRepository.findAll(pageable);
	}
	
	public Brand saveBrand(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public Brand getBrand(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find any brand with id " + id);
		}
	}
	
	public void deleteBrand(Integer id) throws BrandNotFoundException {
		Long countById = brandPagingRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with id " + id);
		}
		brandRepository.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreating = (id == null || id == 0);
		Brand brand = brandPagingRepository.findByName(name);
		
		if(isCreating) {
			if(brand != null) {
				return "Duplicate";
			}
		} else {
			if(brand != null && brand.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
}
