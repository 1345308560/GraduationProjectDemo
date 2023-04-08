package com.example.demo.dao;


import com.example.demo.entity.Rights;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface RightsRepository extends CrudRepository<Rights, String> {

    //查询全部权限，返回user和user_rights
    @Query(value="select u.*,r.rights_buy,r.rights_sell from user u join rights r on u.num=r.uid where u.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllUsersRights(Integer pagenum, Integer pagesize);

    //插入用户权限
    @Modifying
    @Transactional
    @Query(value = "insert into rights (uid,rights_buy,rights_sell) " +
            "values (?1, ?2, ?3)", nativeQuery = true)
    void createOneUserRights( String uid, Integer rights_buy,Integer rights_sell);


}
