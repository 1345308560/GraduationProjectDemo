package com.example.demo.service;

import com.example.demo.dao.GoodsRepository;
import com.example.demo.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    public  Iterable<Goods> findAll() {
        return goodsRepository.findAll();
    }

    public Optional<Goods> findById(Integer id){
        return goodsRepository.findById(id);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteGoods(Integer id){
        return goodsRepository.deleteGoods(id);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int recoverGoods(Integer id){
        return goodsRepository.recoverGoods(id);
    }
}
