package com.example.demo.dao;


import com.example.demo.entity.Rights;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RightsRepository extends CrudRepository<Rights, String> {

    @Query(value="select * from rights where uid = ?1" ,nativeQuery=true)
    Optional<Rights> findByUid(String uid);

    //查询全部权限，返回user和user_rights
    @Query(value="select u.*,r.rights_buy,r.rights_sell from user u join rights r on u.num=r.uid where u.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllUsersRights(Integer pagenum, Integer pagesize);

    //插入用户权限
    @Modifying
    @Transactional
    @Query(value = "insert into rights (uid,rights_buy,rights_sell) " +
            "values (?1, ?2, ?3)", nativeQuery = true)
    void createOneUserRights( String uid, Integer rights_buy,Integer rights_sell);

    //修改用户buy权限
    @Modifying
    @Query(value="update rights set rights.rights_buy = 1 where uid = ?1" ,nativeQuery=true)
    int updateRights_buy1(String uid);//修改为可以买

    @Modifying
    @Query(value="update rights set rights.rights_buy = 0 where uid = ?1" ,nativeQuery=true)
    int updateRights_buy0(String uid);//修改为不可以买

    //修改用户sell权限
    @Modifying
    @Query(value="update rights set rights.rights_sell = 1 where uid = ?1" ,nativeQuery=true)
    int updateRights_sell1(String uid);//修改为可以卖

    @Modifying
    @Query(value="update rights set rights.rights_sell = 0 where uid = ?1" ,nativeQuery=true)
    int updateRights_sell0(String uid);//修改为不可以卖

}
