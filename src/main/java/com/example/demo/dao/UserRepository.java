package com.example.demo.dao;


import com.example.demo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    @Override
    @Query(value="select * from user" ,nativeQuery=true)
    Iterable<User> findAll();

    @Query(value="select * from user where id = ?1" ,nativeQuery=true)
    Optional<User> findById(Integer id);

    /**
     * 通过手机号查找
     */
    @Query(value="select * from user where phone = ?1" ,nativeQuery=true)
    Optional<User> findByPhone(String phone);

    /**
     * 通过学号查找
     */
    @Query(value="select * from user where num = ?1" ,nativeQuery=true)
    Optional<User> findByNum(String num);

    /**
     * 通过用户名查找
     */
    @Query(value="select * from user where username = ?1" ,nativeQuery=true)
    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value="update user set user.display = 1 where id = ?1" ,nativeQuery=true)
    int deleteUser(Integer id);//软删除，修改display即可

    @Modifying
    @Query(value="update user set user.display = 0 where id = ?1" ,nativeQuery=true)
    int recoverUser(Integer id);//恢复删除


    // 查询总条数
    @Query(value="select count(*) from user where user.display = 0" ,nativeQuery=true)
    int countGoods(String query);
    // 分页查询
    @Query(value="select * from user where user.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<User> findAllUsers(Integer pagenum, Integer pagesize, String query);

    /**
     * 插入一条User
     */
    @Modifying
    @Transactional
    @Query(value = "insert into user (username,password,num,phone,token,uuid) " +
            "values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void createOneUser( String username, String password,
                        String num, String phone, String token,String uuid);
}