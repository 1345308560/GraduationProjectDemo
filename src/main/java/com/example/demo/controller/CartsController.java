package com.example.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.R;
import com.example.demo.service.CartsService;
import com.example.demo.service.GoodsService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "carts")
public class CartsController {

    @Autowired
    CartsService cartsService;

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>>> getAllCarts(@RequestParam Map<String,Object> map){
        Integer pagenum= Integer.valueOf((String) map.get("pagenum"));
        Integer pagesize= Integer.valueOf((String) map.get("pagesize"));
        String query= map.get("query").toString();
        String kind=map.get("kind").toString();
        if (pagenum==null){
            pagenum=1;
        }
        if (pagesize==null){
            pagesize=10;
        }
        if(query==""){
            // 获取总页数
            Integer total=userService.getTotalPage();
            log.info("query为空");
            List<Map<String, Object>> res=cartsService.findAllCarts(pagenum,pagesize);
            //获取json_value
            Integer size= res.size();
            for (int i=0;i<size;i++){
                Map<String, Object> stringObjectMap = res.get(i);
                if(stringObjectMap.get("json_value")==null){
                    continue;
                }
                JSONObject details =JSONObject.parseObject(stringObjectMap.get("json_value").toString());
                List<String> goods_id= JSON.parseArray(details.getJSONArray("goods_id").toJSONString(),String.class);
                List<String> num= JSON.parseArray(details.getJSONArray("num").toJSONString(),String.class);
                List<String> goods_title=goodsService.findGoodsTitle(goods_id);
                BigDecimal price=goodsService.findGoodsPrice(goods_id);
                JSONObject result = JSON.parseObject(JSON.toJSONString(stringObjectMap));

                result.remove("json_value");
                result.put("title",goods_title);
                result.put("num",num);
                result.put("price",price);
//                stringObjectMap.remove("json_value");
//                stringObjectMap.put("title",goods_title);
//                stringObjectMap.put("num",num);
                res.remove(i);
                res.add(i,result);
            }
            return R.success(res).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            Integer total=userService.getCertainPage(kind,query);
            List<Map<String, Object>> res =cartsService.findCertainCarts(pagenum, pagesize, kind, query);
            //获取json_value
            Integer size= res.size();
            for (int i=0;i<size;i++){
                Map<String, Object> stringObjectMap = res.get(i);
                JSONObject details =JSONObject.parseObject(stringObjectMap.get("json_value").toString());
                List<String> goods_id= JSON.parseArray(details.getJSONArray("goods_id").toJSONString(),String.class);
                List<String> num= JSON.parseArray(details.getJSONArray("num").toJSONString(),String.class);
                List<String> goods_title=goodsService.findGoodsTitle(goods_id);
                BigDecimal price=goodsService.findGoodsPrice(goods_id);
                JSONObject result = JSON.parseObject(JSON.toJSONString(stringObjectMap));

                result.remove("json_value");
                result.put("title",goods_title);
                result.put("num",num);
                result.put("price",price);
//                stringObjectMap.remove("json_value");
//                stringObjectMap.put("title",goods_title);
//                stringObjectMap.put("num",num);
                res.remove(i);
                res.add(i,result);
            }
            return R.success(res).add("total",total);
        }
    }
}
