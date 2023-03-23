package com.example.demo.service;

import com.example.demo.dao.GoodsRepository;
import com.example.demo.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    public  Iterable<Goods> findAll() {
        return goodsRepository.findAll();
    }
}
