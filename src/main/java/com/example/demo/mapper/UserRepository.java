package com.example.demo.mapper;

import com.example.demo.dao.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    Iterable<User> findAll();
}