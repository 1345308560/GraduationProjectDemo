package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public @ResponseBody R<Goods> deleteGoods(@RequestParam Map<String,Object> head){
        Integer goodsId= Integer.valueOf((String) head.get("id"));
        log.info("{}",goodsId);
        if(!goodsService.findById(goodsId).isPresent()){
            return R.error("商品删除失败");
        }
        goodsService.deleteGoods(goodsId);
        return R.success(null,"商品删除成功");
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
    public @ResponseBody R<List<Map<String,Object>>> getAllGoods(@RequestParam Map<String,Object> map){
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
            Integer total=goodsService.countGoods();
            log.info("query为空");
            return R.success(goodsService.findAllGoods(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            Integer total = goodsService.getCertainPage(kind, query);
            return R.success(goodsService.findCertainGoods(pagenum, pagesize, kind, query)).add("total", total);
        }
    }

    @PostMapping("/add")
    public @ResponseBody R<Optional<Goods>> addGoods(@RequestBody Map map){
        String goods_id=map.get("goods_id").toString();
        String num=map.get("uid").toString();
        String title=map.get("title").toString();
        Integer quantity=Integer.valueOf(map.get("quantity").toString());
        Integer type=Integer.valueOf(map.get("type").toString());
        Integer degree=Integer.valueOf(map.get("degree").toString());
        BigDecimal price= new BigDecimal(map.get("price").toString());
        BigDecimal price_ago=new BigDecimal(map.get("price_ago").toString());
        String description=map.get("description").toString();
        String img1=map.get("img1").toString();
        String img2=map.get("img2").toString();
        String img3=map.get("img3").toString();
        if(!userService.findByNum(num).isPresent()){
            return R.error("用户不存在，商品添加失败");
        }
        if(goodsService.findByGoodsId(goods_id).isPresent())
        {
            return R.error("商品已存在");
        }
        Integer uid=userService.findByNum(num).get().getId();
        Optional<Goods> newGoods=goodsService.addOneGoods(goods_id,title,uid,degree,type,price_ago,price,quantity,description,img1,img2,img3,goods_id);
        return R.success(newGoods,"添加商品成功");
    }

    @PutMapping(path = "")
    public @ResponseBody R<Optional<Goods>> updateGoods(@RequestParam Map<String,Object> head,@RequestBody Map map){
        Integer id= Integer.valueOf((String) head.get("id"));
        String goods_id=map.get("goods_id").toString();
        String num=map.get("uid").toString();
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
        if(goods_id=="" || num == "" || title == ""  ){
            return R.error("信息不完整");
        }
        if(!userService.findByNum(num).isPresent()){
            return R.error("用户不存在");
        }
        Integer uid=userService.findByNum(num).get().getId();
        Optional<Goods> goods=goodsService.updateGoods(id,goods_id,uid,title,quantity,type,degree,price,price_ago,description,img1,img2,img3);
        return R.success(goods,"商品修改成功");
    }

    //读取图片
    @GetMapping("/loadimg/**")

    public void getImg2(HttpServletResponse response, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI=request.getRequestURI();
        String img = StringUtils.substringAfter(requestURI,"loadimg/");
        String imgPath="/usr/img/goods/";
        String url = imgPath+img;
        log.info("{}",url);
        File file = new File(url);//imgPath为服务器图片地址

        if(file.exists() && file.isFile()){
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
