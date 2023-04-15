package com.example.demo.service;

import com.example.demo.dao.CartsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartsService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CartsRepository cartsRepository;

    @Autowired
    UserService userService;

    public List<Map<String,Object>> findAllCarts(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = userService.getTotalPage();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        List<Map<String,Object>> res=cartsRepository.findAllCarts(pagenum, pagesize);
        return res;
    }

    public List<Map<String, Object>> findCertainCarts(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total =userService.getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // @Query(value="select o.*,u1.username as seller,u1.qq as sell_qq,u2.username as buyers,u2.qq as buy_qq from orders o join user u1 on o.uid1=u1.id " +
        //            "join user u2 on o.uid2=u2.id where o.display = 0 limit ?1, ?2" ,nativeQuery=true)
        pagenum=(pagenum-1)*pagesize;
        String table="u";

        String sql="select c.*,u.username,u.phone,u.num as number" +
                " from carts c join user u on c.uid=u.id  " +
                "where c.display = 0 and "+table+"."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }
}
