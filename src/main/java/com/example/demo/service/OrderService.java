package com.example.demo.service;


import com.example.demo.dao.OrderRepository;
import com.example.demo.entity.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class OrderService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    GoodsService goodsService;

    public Map<String,Object> findById(Integer id){
        return orderRepository.findById(id);
    }

    public int getCertainPage(String kind,String query){
        String sql="select a.* from orders a join orders b " +
                "on a.id = b.id " +
                "where a.display=0 and b."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

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

    public List<Map<String, Object>> findCertainOrders(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total =getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
    // @Query(value="select o.*,u1.username as seller,u1.qq as sell_qq,u2.username as buyers,u2.qq as buy_qq from orders o join user u1 on o.uid1=u1.id " +
        //            "join user u2 on o.uid2=u2.id where o.display = 0 limit ?1, ?2" ,nativeQuery=true)
        pagenum=(pagenum-1)*pagesize;
        String sql="select o.*,u1.username as seller,u1.qq as sell_qq,u2.username as buyers,u2.qq as buy_qq" +
                " from orders o join user u1 on o.uid1=u1.id  " +
                "join user u2 on o.uid2=u2.id"+
                "where o.display = 0 and o."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteOrders(Integer id){
        return orderRepository.deleteOrders(id);
    }
}
