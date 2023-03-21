package com.example.demo.mapper;

import com.example.demo.dao.Admin;
import org.springframework.data.repository.CrudRepository;
public interface AdminRepository extends CrudRepository<Admin, Integer>{
    @Override
    Iterable<Admin> findAll();
}
