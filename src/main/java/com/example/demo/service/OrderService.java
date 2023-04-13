package com.example.demo.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.GoodsRepository;
import com.example.demo.dao.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class OrderService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    GoodsService goodsService;

    public List<Map<String,Object>> findAllOrder(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countOrder();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        List<Map<String,Object>> res=orderRepository.findAllOrder(pagenum, pagesize);
        return res;
    }

    public int countOrder(){
        return orderRepository.countOrder();
    }

    public List<String> findGoodsTitle(List<String> goodsId) {
        Integer size=goodsId.size();
        List<String> goodsTitle =new ArrayList<>();
        for(int i=0;i<size;i++){
            goodsTitle.add(goodsService.findGoodsTitle(goodsId.get(i)));
        }
        return goodsTitle;
    }
}
