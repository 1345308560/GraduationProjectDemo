package com.example.demo.dao;

import com.example.demo.entity.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, String>{
    /**
     * 查找所有的admin
     * @return Iterable<Admin>
     */
    @Override
    @Query(value="select * from admin" ,nativeQuery=true)
    Iterable<Admin> findAll();

    @Override
    @Query(value="select * from admin where id = ?1" ,nativeQuery=true)
    Optional<Admin> findById(String id);
    /**
     * 插入一条Admin
     */
    @Modifying
    @Transactional
    @Query(value = "insert into admin (id,username,password,num,phone,token,create_at,update_at,display,uuid) " +
            "values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)", nativeQuery = true)
    void createOneAdmin(Integer id,String username, String password,
                        String num,String phone,String token,
                        Date create_at,Date update_at,
                        Integer display,String uuid);

    /**
     * 插入许多条
     */
    @Modifying
    @Transactional
    @Query(value = "insert into user (name, age) values (:name, :age)", nativeQuery = true)
    void batchInsert(@Param("name") String name, @Param("age") Integer age);

    /**
     * 通过手机号查找
     */
    @Query(value="select * from admin where phone = ?1" ,nativeQuery=true)
    Optional<Admin> findByPhone(String phone);

    /**
     * 通过学号号查找
     */
    @Query(value="select * from admin where num = ?1" ,nativeQuery=true)
    Optional<Admin> findByNum(String num);
}
