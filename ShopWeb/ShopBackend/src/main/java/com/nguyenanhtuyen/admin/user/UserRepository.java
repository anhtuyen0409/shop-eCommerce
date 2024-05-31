package com.nguyenanhtuyen.admin.user;

import org.springframework.data.repository.CrudRepository;

import com.nguyenanhtuyen.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
