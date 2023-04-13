package com.example.demo.dao;

import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface OrderRepository  extends CrudRepository<Orders, String> {

    @Query(value="select count(*) from orders where orders.display = 0" ,nativeQuery=true)
    int countOrder();

    @Query(value="select o.*,u1.username as seller,u1.qq as sell_qq,u2.username as buyers,u2.qq as buy_qq from orders o join user u1 on o.uid1=u1.id " +
            "join user u2 on o.uid2=u2.id where o.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllOrder(Integer pagenum, Integer pagesize);

}
