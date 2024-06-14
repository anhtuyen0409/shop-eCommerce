package com.nguyenanhtuyen.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.nguyenanhtuyen.common.entity.User;

public interface UserPagingRepository extends PagingAndSortingRepository<User, Integer>{

}
