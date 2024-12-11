package com.nguyenanhtuyen.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nguyenanhtuyen.admin.service.BrandService;
import com.nguyenanhtuyen.admin.service.ProductService;
import com.nguyenanhtuyen.common.entity.Brand;
import com.nguyenanhtuyen.common.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	
	@GetMapping("/products/create-new-product")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
		productService.save(product);
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
}
