package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CartsRepository;
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

@Slf4j
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

    public int addToCart(Integer userId, String goodsId, Integer num) {
        String json_value=findUserCarts(userId);
        JSONObject goods =JSONObject.parseObject(json_value);
        if(goods.get("goods_id").equals("")){
            List<String> goods_id=new ArrayList<>();
            List<String> quantity=new ArrayList<>();
            goods_id.add(goodsId);
            goods.remove("goods_id");
            goods.put("goods_id",goods_id);
            quantity.add(num.toString());
            goods.remove("num");
            goods.put("num",quantity);
            return updateGoods(userId,goods.toString());
        }

        List<String> goods_id= JSON.parseArray(goods.getJSONArray("goods_id").toJSONString(),String.class);
        List<String> quantity= JSON.parseArray(goods.getJSONArray("num").toJSONString(),String.class);
        int res=isExist(goods_id,goodsId);
        if(res!=-1){
            //购物车中存在该商品，在之前的数量上加上新增数量
            Integer number=num+Integer.valueOf(quantity.get(res).toString());
            quantity.remove(res);
            quantity.add(res,number.toString());
            goods.remove("num");
            goods.put("num",quantity);

        }else{
            //购物车中不存在该商品，新增该商品和对应数量
            goods_id.add(goodsId);
            goods.remove("goods_id");
            goods.put("goods_id",goods_id);
            quantity.add(num.toString());
            goods.remove("num");
            goods.put("num",quantity);
        }
        return updateGoods(userId,goods.toString());
    }

    private int updateGoods(Integer userId, String goods) {
        return cartsRepository.updateGoods(userId,goods);
    }

    public String findUserCarts(Integer userId){
        return cartsRepository.findUserCarts(userId);
    }

    public int isExist(List<String> goods_id,String goodsId){
        int size= goods_id.size();
        for (int id=0;id<size;id++){
            boolean match=goods_id.get(id).equals(goodsId);
            if(match){
                return id;
            }
        }
        return -1;
    }
}
