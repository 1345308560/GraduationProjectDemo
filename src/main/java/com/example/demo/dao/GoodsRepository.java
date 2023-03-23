package com.example.demo.dao;

import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GoodsRepository extends CrudRepository<Goods, Integer> {
    @Override
    @Query(value="select * from goods" ,nativeQuery=true)
    Iterable<Goods> findAll();
}
