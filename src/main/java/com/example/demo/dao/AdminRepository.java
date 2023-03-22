package com.example.demo.dao;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, String>{
    @Override
    @Query(value="select * from admin" ,nativeQuery=true)
    Iterable<Admin> findAll();

    @Override
    @Query(value="select * from admin where id = ?1" ,nativeQuery=true)
    Optional<Admin> findById(String id);

}
