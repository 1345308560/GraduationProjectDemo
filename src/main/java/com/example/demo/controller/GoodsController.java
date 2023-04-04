package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import com.example.demo.service.GoodsService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(path="/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;

    @GetMapping(path="/allGoods")
    public @ResponseBody Iterable<Goods> getAllAdmins() {
        // This returns a JSON or XML with the goods
        return goodsService.findAll();
    }
    // generateGoods()方法接受参数num，生成num个随机商品，插入到数据库中，无返回值
    @PostMapping(path="/generateGoods")
    public @ResponseBody void generateGoods(@RequestParam Integer num) {
        goodsService.insertGoods(num);
    }

    @PutMapping(path = "/delete")
    public @ResponseBody R<Goods> deleteGoods(@RequestBody Map map){
        Integer goodsId= (Integer) map.get("id");
        goodsService.deleteGoods(goodsId);
        if(!goodsService.findById(goodsId).isPresent()){
            return R.error("商品删除失败");
        }
        Goods goods=goodsService.findById(goodsId).get();
        if (goods.getDisplay()==true){
            return R.success(null,"商品删除成功");
        }
        return R.error("商品删除失败");
    }
    /**
     * Get请求：
     * 请求接口：/goods/all
     * 携带数据：queryInfo: {
     *                 query: '',//查询参数（比如账号，电话）
     *                 pagenum: 1,//当前页码值
     *                 pagesize: 10//每页显示条数
     *             }
     * （查询参数如果为空那么视为全部数据，当前页码值为空则为1，显示条数非空）
     * 返回值就为查询的那几条用户数据，
     * 如果下一页有数据返回状态200，message：查询成功
     * 下一页没有数据返回其他状态值，message：无更多数据
     */
    @GetMapping(path = "/all")
    public @ResponseBody R<List<Goods>> getAllGoods(@RequestParam Map map){
        Integer pagenum= Integer.valueOf((String) map.get("pagenum"));
        Integer pagesize= Integer.valueOf((String) map.get("pagesize"));
        String query= (String) map.get("query");
        if (pagenum==null){
            pagenum=1;
        }
        if (pagesize==null){
            pagesize=10;
        }
        if (query==null){
            query="";
        }
        Integer total=goodsService.getTotalPage(query);
        return R.success(goodsService.findAllGoods(pagenum,pagesize,query)).add("total",total);
    }

    @PostMapping("/add")
    public @ResponseBody R<Optional<Goods>> addGoods(@RequestBody Map map){
        String goods_id=map.get("goods_id").toString();
        String uid=map.get("uid").toString();
        String title=map.get("title").toString();
        Integer quantity=(Integer) map.get("quantity");
        Integer type=(Integer) map.get("type");
        Integer degree=(Integer)map.get("degree");
        BigDecimal price= new BigDecimal(map.get("price").toString());
        BigDecimal price_ago=new BigDecimal(map.get("price_ago").toString());
        String description=map.get("description").toString();
        String img1=map.get("img1").toString();
        String img2=map.get("img2").toString();
        String img3=map.get("img3").toString();
        if(!userService.findByNum(uid).isPresent()){
            return R.error("用户不存在，商品添加失败");
        }
        if(goodsService.findByGoodsId(goods_id).isPresent())
        {
            return R.error("商品已存在");
        }
        Optional<Goods> newGoods=goodsService.addOneGoods(goods_id,title,uid,degree,type,price_ago,price,quantity,description,img1,img2,img3,goods_id);
        return R.success(newGoods,"添加商品成功");
    }
}
