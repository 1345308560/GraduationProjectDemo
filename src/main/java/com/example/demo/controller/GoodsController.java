package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path="/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Goods> getAllAdmins() {
        // This returns a JSON or XML with the goods
        return goodsService.findAll();
    }

    @PostMapping(path = "/updateState")
    public @ResponseBody R<Goods> updateGoodsState(@RequestBody Map map){
        Integer goodsId= (Integer) map.get("id");
        Boolean goodsDisplay=(Boolean) map.get("display");
        if(!goodsService.findById(goodsId).isPresent()){
            return R.error("商品不存在");
        }
        Goods goods=goodsService.findById(goodsId).get();
        if (goods.getDisplay()!=goodsDisplay){
            return R.success(goods);
        }
        if(goodsDisplay==false){
            goodsService.deleteGoods(goodsId);
            goods.setDisplay(true);
            return R.success(goods);
        }
        goodsService.recoverGoods(goodsId);
        goods.setDisplay(false);
        return R.success(goods);
    }
}
