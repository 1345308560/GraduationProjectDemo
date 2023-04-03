package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path="/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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
}
