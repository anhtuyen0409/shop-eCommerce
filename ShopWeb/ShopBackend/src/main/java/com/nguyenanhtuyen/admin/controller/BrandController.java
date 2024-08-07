package com.nguyenanhtuyen.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.nguyenanhtuyen.admin.exporter.BrandCsvExporter;
import com.nguyenanhtuyen.admin.exporter.BrandExcelExporter;
import com.nguyenanhtuyen.admin.exporter.BrandPdfExporter;
import com.nguyenanhtuyen.admin.service.BrandService;
import com.nguyenanhtuyen.admin.service.CategoryService;
import com.nguyenanhtuyen.admin.util.FileUploadUtil;
import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/brands/page/{pageNumber}")
	public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
		
		Page<Brand> page = brandService.listByPage(pageNumber, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();
		
		long startCount = (pageNumber - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "brands/brands";
	}
	
	@GetMapping("/brands/create-new-brand")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/test";
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
			
			return "brands/test";
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
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrands, response);
	}
	
	@GetMapping("/brands/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandExcelExporter excelExporter = new BrandExcelExporter();
		excelExporter.export(listBrands, response);
	}
	
	@GetMapping("/brands/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandPdfExporter pdfExporter = new BrandPdfExporter();
		pdfExporter.export(listBrands, response);
	}
}
