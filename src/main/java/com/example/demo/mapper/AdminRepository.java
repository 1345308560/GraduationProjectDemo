package com.example.demo.mapper;

import com.example.demo.dao.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, String>{
    @Override
    Iterable<Admin> findAll();

    @Override
    Optional<Admin> findById(String number);

}
