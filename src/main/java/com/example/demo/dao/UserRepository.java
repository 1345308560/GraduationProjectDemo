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
    /**
     * 插入创建的随机用户
     * private String username;
     *
     *     private String password;
     *
     *     private String num;//学号
     *
     *     private String phone;//手机号
     *
     *     private String qq;//qq号
     *
     *     private String addr;
     *
     *     private Integer ban;
     *
     *     private String token;
     *
     *     private String icon; // 头像
     *
     *     private Date create_at;
     *
     *     private Date update_at;
     *
     *     private Integer display;
     *
     *     private String uuid;
     */
    @Modifying
    @Transactional
    @Query(value = "insert into user (username,password,num,phone,qq,addr,ban," +
            "token,icon,create_at,update_at,display,uuid) " +
            "values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)",
            nativeQuery = true)
    void createOneUser2(String username, String password,
                        String num, String phone, String qq, String addr, Integer ban,
                        String token, String icon, Date create_at, Date update_at,
                        Integer display, String uuid);
}