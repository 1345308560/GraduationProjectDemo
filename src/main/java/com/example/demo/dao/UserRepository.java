package com.example.demo.dao;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    @Query(value="select * from user" ,nativeQuery=true)
    Iterable<User> findAll();
}