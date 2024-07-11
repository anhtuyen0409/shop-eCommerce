package com.nguyenanhtuyen.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nguyenanhtuyen.admin.repository.BrandPagingRepository;
import com.nguyenanhtuyen.admin.repository.BrandRepository;
import com.nguyenanhtuyen.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

	@MockBean
	private BrandRepository brandRepository;
	
	@MockBean 
	private BrandPagingRepository brandPagingRepository;
	
	@InjectMocks
	private BrandService brandService;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Dell";
		
		Brand brand = new Brand(name);
		Mockito.when(brandPagingRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "test";
		
		Mockito.when(brandPagingRepository.findByName(name)).thenReturn(null);
		String result = brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name = "Dell";
		
		Brand brand = new Brand(id, name);
		Mockito.when(brandPagingRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "test";
		
		Brand brand = new Brand(id, name);
		Mockito.when(brandPagingRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(id, name);
		
		assertThat(result).isEqualTo("OK");
	}
}
