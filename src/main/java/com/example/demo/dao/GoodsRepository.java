package com.example.demo.dao;

import com.example.demo.entity.Goods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoodsRepository extends CrudRepository<Goods, Integer> {
    @Override
    @Query(value="select * from goods" ,nativeQuery=true)
    Iterable<Goods> findAll();


    @Query(value="select * from goods where id = ?1" ,nativeQuery=true)
    Optional<Goods> findById(Integer id);

    @Modifying
    @Query(value="update goods set goods.display = 1 where id = ?1" ,nativeQuery=true)
    public int deleteGoods(Integer id);//软删除，修改display即可

    @Modifying
    @Query(value="update goods set goods.display = 0 where id = ?1" ,nativeQuery=true)
    public int recoverGoods(Integer id);//恢复删除

}
