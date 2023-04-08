package com.example.demo.dao;


import com.example.demo.entity.Rights;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface RightsRepository extends CrudRepository<Rights, String> {

    @Query(value="select u.*,r.rights_buy,r.rights_sell from user u join rights r on u.num=r.uid where u.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllUsersRights(Integer pagenum, Integer pagesize);



}
