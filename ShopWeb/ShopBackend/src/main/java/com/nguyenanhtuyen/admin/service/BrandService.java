package com.nguyenanhtuyen.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.exception.BrandNotFoundException;
import com.nguyenanhtuyen.admin.repository.BrandPagingRepository;
import com.nguyenanhtuyen.admin.repository.BrandRepository;
import com.nguyenanhtuyen.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private BrandPagingRepository brandPagingRepository;
	
	public List<Brand> listAll() {
		return (List<Brand>) brandRepository.findAll();
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
}
