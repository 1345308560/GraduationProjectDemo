package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.R;
import com.example.demo.dao.GoodsRepository;
import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class GoodsService {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private GoodsRepository goodsRepository;

    public  Iterable<Goods> findAll() {
        return goodsRepository.findAll();
    }

    public Optional<Goods> findById(Integer id){
        return goodsRepository.findById(id);
    }

    public Optional<Goods> findByGoodsId(String goods_id){
        return goodsRepository.findByGoodsId(goods_id);
    }

    public List<String> findGoodsTitle(List<String> goodsId) {
        Integer size=goodsId.size();
        List<String> goodsTitle =new ArrayList<>();
        for(int i=0;i<size;i++){
            goodsTitle.add(findGoodsTitle(goodsId.get(i)));
        }
        return goodsTitle;
    }

    public List<BigDecimal> findGoodsPrice(List<String> goodsId) {
        Integer size=goodsId.size();
        List<BigDecimal> goodsPrice =new ArrayList<>();
        for(int i=0;i<size;i++){
            goodsPrice.add(findGoodsPrice(goodsId.get(i)));
        }
        return goodsPrice;
    }

    public BigDecimal findPriceCount(List<String> goodsId) {
        Integer size=goodsId.size();
        BigDecimal price=new BigDecimal("0.00");
        List<BigDecimal> prices= new ArrayList<>();
        prices.add(price);
        for(int i=0;i<size;i++){
           prices.add(prices.get(i).add(findGoodsPrice(goodsId.get(i))));
        }
        return prices.get(size);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteGoods(Integer id){
        return goodsRepository.deleteGoods(id);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int recoverGoods(Integer id){
        return goodsRepository.recoverGoods(id);
    }
    // 调用generateGoods()方法生成一个随机商品，插入到数据库中
    public void insertGoods(int num){
        // 生成num个随机good，插入到数据库中
        for(int i = 0; i < num; i++){
            Goods good = generateGoods();
            goodsRepository.insertGoods(good.getId().toString(), good.getUuid(), good.getTitle(),
                    good.getUid(), good.getDegree(), good.getType(),
                    good.getPrice_ago(), good.getPrice(),
                    good.getQuantity(), good.getDescription(), good.getImg1(),
                    good.getImg2(), good.getImg3(), good.getCreate_at(),
                    good.getUpdate_at(), good.getDisplay()?1:0);
        }
    }

    // 生成一个随机商品，返回good
    public Goods generateGoods(){
        Goods good = new Goods();
        // 设置good的id为1-10000的随机数
        good.setId((int)(Math.random()*10000));
        // 设置good的uuid字段为随机生成的一个uuid
        good.setUuid(java.util.UUID.randomUUID().toString());
        // 设置good的商品名为一个随机中文的商品名字
        String[] goods = {"苹果", "香蕉", "橘子", "梨子", "西瓜", "草莓", "葡萄",
                "芒果", "菠萝", "柠檬", "橙子", "桃子", "李子", "樱桃", "哈密瓜",
                "猕猴桃", "火龙果", "榴莲", "龙眼", "荔枝"};
        int randomIndex = (int)(Math.random()*20);
        good.setTitle(goods[randomIndex]);
        good.setUid(1);
        good.setDegree((int)(Math.random()*10)+1);
        int randomIndex3 = (int)(Math.random()*20);
        good.setType(randomIndex3);
        good.setPrice_ago(BigDecimal.valueOf(Math.random()*1000));
        good.setPrice(BigDecimal.valueOf(Math.random()*1000));
        good.setQuantity((int)(Math.random()*100)+1);
        String[] descriptions = {"这是一个好商品", "这个商品很棒", "这个商品非常实用", "这个商品非常美观",
                "这个商品非常耐用", "这个商品非常舒适", "这个商品非常时尚", "这个商品非常便宜", "这个商品非常高档",
                "这个商品非常好用", "这个商品非常方便", "这个商品非常实惠", "这个商品非常好看", "这个商品非常有特色",
                "这个商品非常有品质", "这个商品非常有用", "这个商品非常有价值", "这个商品非常有趣", "这个商品非常有创意",
                "这个商品非常有个性"};
        int randomIndex2 = (int)(Math.random()*20);
        good.setDescription(descriptions[randomIndex2]);
        good.setImg1("https://www.example.com/img1");
        good.setImg2("https://www.example.com/img2");
        good.setImg3("https://www.example.com/img3");

        Date startDate = Date.from(Instant.parse("2022-01-01T00:00:00.00Z"));
        Date endDate = new Date();
        long randomTime = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
        Date randomDate = new Date(randomTime);
        long randomTime2 = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
        Date randomDate2 = new Date(randomTime2);
        good.setCreate_at(randomDate);
        good.setUpdate_at(randomDate2);
        good.setDisplay(false);
        return good;
    }


    public List<Map<String,Object>> findAllGoods(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countGoods();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        return goodsRepository.findAllGoods(pagenum, pagesize);
    }

    public List<Map<String,Object>> findAllWantGoods(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countWantGoods();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        List<Map<String,Object>> res=goodsRepository.findAllWantGoods(pagenum, pagesize);
        //把负数修正为正数
        Integer size= res.size();

        for (int i=0;i<size;i++){
            Map<String, Object> stringObjectMap = res.get(i);
            Integer num=Integer.valueOf(stringObjectMap.get("quantity").toString());
            JSONObject result = JSON.parseObject(JSON.toJSONString(stringObjectMap));

            result.remove("json_value");
            result.remove("quantity");
            result.put("quantity",-num);

            res.remove(i);
            res.add(i,result);
        }
        return res;
    }

    //query不为空时，查询特定字段，使用entityManager 创建本地查询自定义sql
    public List<Map<String,Object>> findCertainGoods(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        String table=null;
        if(kind.equals("username")){
            table="u";
        }
        else{
            table="g";
        }
        pagenum=(pagenum-1)*pagesize;
        String sql="select g.*,u.username,u.num from goods g join user u " +
                "on g.uid = u.id " +
                "where g.display=0 and g.quantity>0 " +
                "and "+ table+"."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }

    public List<Map<String,Object>> findCertainWantGoods(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainWantPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        String table=null;
        if(kind.equals("username")){
            table="u";
        }
        else{
            table="g";
        }
        pagenum=(pagenum-1)*pagesize;
        String sql="select g.*,u.username,u.num from goods g join user u " +
                "on g.uid = u.id " +
                "where g.display=0 and g.quantity<0 " +
                "and "+ table+"."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> res = query1.getResultList();
        Integer size= res.size();

        for (int i=0;i<size;i++){
            Map<String, Object> stringObjectMap = res.get(i);
            Integer num=Integer.valueOf(stringObjectMap.get("quantity").toString());
            JSONObject result = JSON.parseObject(JSON.toJSONString(stringObjectMap));

            result.remove("json_value");
            result.remove("quantity");
            result.put("quantity",-num);

            res.remove(i);
            res.add(i,result);
        }
        return res;
    }

    // 查询商品的总数
    public int countGoods(){
        return goodsRepository.countGoods();
    }

    public int countWantGoods(){
        return goodsRepository.countWantGoods();
    }

    public int getCertainPage(String kind,String query){
        String table=null;
        if(kind.equals("username")) {
            table="u";
        }
        else{
            table="g";
        }
        String sql="select g.*,u.username  from goods g join user u " +
                "on g.uid = u.id where g.display=0 and g.quantity>0 and "+table+"."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

    public int getCertainWantPage(String kind,String query){
        String table=null;
        if(kind.equals("username")) {
            table="u";
        }
        else{
            table="g";
        }
        String sql="select g.*,u.username  from goods g join user u " +
                "on g.uid = u.id where g.display=0 and g.quantity<0 and "+table+"."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

    public Optional<Goods> addOneGoods(String goods_id, String title, Integer uid,
                                       Integer degree, Integer type, BigDecimal price_ago, BigDecimal price,
                                       Integer quantity, String description, String img1, String img2,
                                       String img3,String uuid){
        goodsRepository.insertOneGoods(goods_id,title,uid,degree,type,price_ago,price,quantity,description,img1,img2,img3,uuid);
        return goodsRepository.findByGoodsId(goods_id);
    }

    @Transactional
    public Optional<Goods> updateGoods(Integer id,String goods_id,Integer uid,String title,Integer quantity,
                                       Integer type,Integer degree,BigDecimal price,BigDecimal price_ago,
                                       String description,String img1,String img2,String img3){
        String sql="update goods set goods.goods_id="+goods_id+
                ",goods.title='"+title+
                "',goods.uid="+uid+
                ",goods.degree="+degree+
                ",goods.type="+type+
                ",goods.price_ago="+price_ago+
                ",goods.price="+price+
                ",goods.quantity="+quantity+
                ",goods.description='"+description+
                "',goods.img1='"+img1+
                "',goods.img2='"+img2+
                "',goods.img3='"+img3+
                "' where goods.id="+id;
        Integer col = entityManager.createNativeQuery(sql).executeUpdate();
        return goodsRepository.findByGoodsId(goods_id);
    }

    public String findGoodsTitle(String goods_id){
        return goodsRepository.findGoodsTitle(goods_id);
    }

    public BigDecimal findGoodsPrice(String goods_id) {
        return goodsRepository.findGoodsPrice(goods_id);
    }
}
