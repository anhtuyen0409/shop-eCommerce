package com.nguyenanhtuyen.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nguyenanhtuyen.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
