package com.example.demo.dao;

import com.example.demo.entity.Goods;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends CrudRepository<Goods, Integer> {
    @Override
    @Query(value="select * from goods" ,nativeQuery=true)
    Iterable<Goods> findAll();


    @Query(value="select * from goods where id = ?1" ,nativeQuery=true)
    Optional<Goods> findById(Integer id);

    @Query(value="select * from goods where goods_id = ?1" ,nativeQuery=true)
    Optional<Goods> findByGoodsId(String goods_id);

    @Modifying
    @Query(value="update goods set goods.display = 1 where id = ?1" ,nativeQuery=true)
    public int deleteGoods(Integer id);//软删除，修改display即可

    @Modifying
    @Query(value="update goods set goods.display = 0 where id = ?1" ,nativeQuery=true)
    public int recoverGoods(Integer id);//恢复删除
    // 插入一个商品
    @Modifying
    @Transactional
    @Query(value="insert into goods(goods_id, uuid, title, uid, degree, type, price_ago, price, quantity, description, img1, img2, img3, create_at, update_at, display) values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16)" ,nativeQuery=true)
    public int insertGoods(String goods_id, String uuid, String title, String uid,
                           Integer degree, Integer type, BigDecimal price_ago, BigDecimal price,
                           Integer quantity, String description, String img1, String img2,
                           String img3, Date create_at, Date update_at, Integer display);
    // 查询总条数
    @Query(value="select count(*) from goods where goods.display = 0" ,nativeQuery=true)
    int countGoods();
    // 分页查询
    @Query(value="select * from goods where goods.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Goods> findAllGoods(Integer pagenum, Integer pagesize);

    /**
     * 插入一条Goods
     */
    @Modifying
    @Transactional
    @Query(value="insert into goods(goods_id, title, uid, degree, type, price_ago, price, quantity, description, img1, img2, img3, uuid) values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)" ,nativeQuery=true)
    public int insertOneGoods(String goods_id, String title, String uid,
                           Integer degree, Integer type, BigDecimal price_ago, BigDecimal price,
                           Integer quantity, String description, String img1, String img2,
                           String img3, String uuid);

}
