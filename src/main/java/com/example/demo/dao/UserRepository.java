package com.example.demo.dao;

import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    @Override
    @Query(value="select * from user" ,nativeQuery=true)
    Iterable<User> findAll();

    @Override
    @Query(value="select * from user where id = ?1" ,nativeQuery=true)
    Optional<User> findById(String id);

    @Query(value="select * from user where number = ?1" ,nativeQuery=true)
    Optional<User> findById(Integer number);

    @Modifying
    @Query(value="update user set user.display = 1 where number = ?1" ,nativeQuery=true)
    public int deleteUser(Integer id);//软删除，修改display即可

    @Modifying
    @Query(value="update user set user.display = 0 where number = ?1" ,nativeQuery=true)
    public int recoverUser(Integer id);//恢复删除
}