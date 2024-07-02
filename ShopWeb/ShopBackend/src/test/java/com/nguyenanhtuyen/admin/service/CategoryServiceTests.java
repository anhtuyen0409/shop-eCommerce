package com.nguyenanhtuyen.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nguyenanhtuyen.admin.repository.CategoryPagingRepository;
import com.nguyenanhtuyen.admin.repository.CategoryRepository;
import com.nguyenanhtuyen.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@MockBean
	private CategoryRepository categoryRepository;
	
	@MockBean 
	private CategoryPagingRepository categoryPagingRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "test";
		
		Category category = new Category(id, name, alias);
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(null);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null;
		String name = "test";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(category);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "test";
		String alias = "test";
		
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(null);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "test";
		
		Category category = new Category(2, name, alias);
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(null);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 1;
		String name = "test";
		String alias = "computers";
		
		Category category = new Category(2, name, alias);
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(category);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "test";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		Mockito.when(categoryPagingRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryPagingRepository.findByAlias(alias)).thenReturn(category);
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}
}
