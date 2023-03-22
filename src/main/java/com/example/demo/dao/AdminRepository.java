package com.example.demo.dao;

import com.example.demo.entity.Admin;
import org.springframework.data.repository.CrudRepository;
public interface AdminRepository extends CrudRepository<Admin, Integer>{
    @Override
    Iterable<Admin> findAll();
}
