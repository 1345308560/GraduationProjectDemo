package com.example.demo.dao;

import com.example.demo.entity.Carts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface CartsRepository extends CrudRepository<Carts, String> {


    @Query(value="select c.*,u.username,u.phone,u.num as number " +
            "from carts c join user u on c.uid=u.id " +
            "where c.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllCarts(Integer pagenum, Integer pagesize);

}
