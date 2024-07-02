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

import com.nguyenanhtuyen.admin.service.CategoryService;
import com.nguyenanhtuyen.admin.util.FileUploadUtil;
import com.nguyenanhtuyen.common.entity.Category;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = categoryService.listAll();
		model.addAttribute("listCategories", listCategories);
		return "categories/categories";
	}
	
	@GetMapping("/categories/create-new-category")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile, 
			RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/category-" + savedCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(category.getImage().isEmpty()) {
				category.setImage("default.png");
			}
		}
		
		categoryService.save(category);
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/update/{id}")
	public String updateCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Category category = categoryService.getCategoryById(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Update Category (ID: " + id + ")");
			
			return "categories/category_form";
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
}
