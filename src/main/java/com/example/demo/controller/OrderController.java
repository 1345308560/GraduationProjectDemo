package com.example.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.R;
import com.example.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>>> getAllOrder(@RequestParam Map<String,Object> map){
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
            Integer total=orderService.countOrder();
            log.info("query为空");

            List<Map<String, Object>> res = orderService.findAllOrder(pagenum, pagesize);
            //获取json_value
            Integer size= res.size();
            for (int i=0;i<size;i++){
                Map<String, Object> stringObjectMap = res.get(i);
                JSONObject details =JSONObject.parseObject(stringObjectMap.get("json_value").toString());
                List<String> goods_id= JSON.parseArray(details.getJSONArray("goods_id").toJSONString(),String.class);
                List<String> num= JSON.parseArray(details.getJSONArray("num").toJSONString(),String.class);
                List<String> goods_title=orderService.findGoodsTitle(goods_id);

                JSONObject result = JSON.parseObject(JSON.toJSONString(stringObjectMap));

                result.remove("json_value");
                result.put("title",goods_title);
                result.put("num",num);
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
//            Integer total = goodsService.getCertainPage(kind, query);
//            return R.success(goodsService.findCertainUsers(pagenum, pagesize, kind, query)).add("total", total);
        return R.error("");
        }
    }
}
