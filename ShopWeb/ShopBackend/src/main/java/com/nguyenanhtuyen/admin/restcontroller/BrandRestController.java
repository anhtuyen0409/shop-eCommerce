package com.nguyenanhtuyen.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenanhtuyen.admin.service.BrandService;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check-unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return brandService.checkUnique(id, name);
	}
	
}
