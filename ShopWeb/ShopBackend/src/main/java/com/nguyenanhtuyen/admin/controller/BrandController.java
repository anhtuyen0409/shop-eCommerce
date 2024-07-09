package com.nguyenanhtuyen.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nguyenanhtuyen.admin.exception.BrandNotFoundException;
import com.nguyenanhtuyen.admin.service.BrandService;
import com.nguyenanhtuyen.admin.service.CategoryService;
import com.nguyenanhtuyen.admin.util.FileUploadUtil;
import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Category;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listAll(Model model) {
		List<Brand> listBrands = brandService.listAll();
		model.addAttribute("listBrands", listBrands);
		return "brands/brands";
	}
	
	@GetMapping("/brands/create-new-brand")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/brands_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, 
			RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.saveBrand(brand);
			String uploadDir = "../brand-logos/brand-" + savedBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(brand.getLogo().isEmpty()) {
				brand.setLogo("default.png");
			}
		}
		
		brandService.saveBrand(brand);
		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
		
		return "redirect:/brands";
	}

	@GetMapping("/brands/update/{id}")
	public String updateBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = brandService.getBrand(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Update Brand (ID: " + id + ")");
			
			return "brands/brands_form";
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			brandService.deleteBrand(id);
			String brandDir = "../brand-logos/brand-" + id;
			FileUploadUtil.removeDir(brandDir);
			redirectAttributes.addFlashAttribute("message", "The brand with id " + id + " has been deleted successfully.");
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/brands";
	}
}
