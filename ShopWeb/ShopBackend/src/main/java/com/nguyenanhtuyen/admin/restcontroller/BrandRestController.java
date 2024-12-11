package com.nguyenanhtuyen.admin.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.admin.dto.CategoryDTO;
import com.nguyenanhtuyen.admin.exception.BrandNotFoundException;
import com.nguyenanhtuyen.admin.exception.BrandNotFoundRestException;
import com.nguyenanhtuyen.admin.service.BrandService;
import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check-unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
		List<CategoryDTO> listCategories = new ArrayList<>();
		
		try {
			Brand brand = brandService.getBrand(brandId);
			Set<Category> categories = brand.getCategories();
			
			for(Category category : categories) {
				CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(categoryDTO);
			}
			
			return listCategories;
		} catch (BrandNotFoundException ex) {
			throw new BrandNotFoundRestException();
		}
	}
	
}
