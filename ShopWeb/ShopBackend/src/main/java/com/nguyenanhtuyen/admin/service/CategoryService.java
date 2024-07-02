package com.nguyenanhtuyen.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.admin.exception.CategoryNotFoundException;
import com.nguyenanhtuyen.admin.repository.CategoryPagingRepository;
import com.nguyenanhtuyen.admin.repository.CategoryRepository;
import com.nguyenanhtuyen.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryPagingRepository categoryPagingRepository;
	
	public List<Category> listAll() {
		List<Category> rootCategories = categoryPagingRepository.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for(Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = rootCategory.getChildren();
			
			for(Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
		}
		
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		
		for(Category subCategory : children) {
			String name = "";
			for(int i=0; i<newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
		}
	}
	
	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categories = categoryRepository.findAll();
		
		for(Category category : categories) {
			if(category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> children = category.getChildren();
				
				for(Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listChildren(categoriesUsedInForm, subCategory, 1);
				}
			}
		}
		
		return categoriesUsedInForm;
	}
	
	private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for(Category subCategory : children) {
			String name = "";
			for(int i=0; i<newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listChildren(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public Category getCategoryById(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (Exception ex) {
			throw new CategoryNotFoundException("Could not find any category with id " + id);
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreating = (id == null || id == 0);
		
		Category categoryByName = categoryPagingRepository.findByName(name);
		if(isCreating) {
			if(categoryByName != null) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = categoryPagingRepository.findByAlias(alias);
				if(categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = categoryPagingRepository.findByAlias(alias);
			if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		
		return "OK";
	}
}
