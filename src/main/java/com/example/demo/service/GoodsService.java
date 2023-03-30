package com.example.demo.service;

import com.example.demo.dao.GoodsRepository;
import com.example.demo.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
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
        good.setUid("1131190256");
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

    public List<Goods> findAllGoods(Integer pagenum, Integer pagesize, String query) {
        // 获取商品的总数
        int total = goodsRepository.countGoods(query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        Integer query1=(pagenum-1)*pagesize;
        return goodsRepository.findAllGoods(query1, pagesize, query);
    }
}
