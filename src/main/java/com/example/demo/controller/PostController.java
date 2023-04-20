package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>>> getAllPost(@RequestParam Map<String,Object> map){
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
            Integer total=postService.countPost();
            log.info("query为空");
            return R.success(postService.findAllPost(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            Integer total = postService.getCertainPage(kind, query);
            return R.success(postService.findCertainPost(pagenum, pagesize, kind, query)).add("total", total);
        }
    }

    @PutMapping(path = "/delete")
    public @ResponseBody R deletePost(@RequestParam Map<String,Object> head){
        Integer postId= Integer.valueOf((String) head.get("id"));
        log.info("{}",postId);
        if(!postService.findById(postId).isPresent()){
            return R.error("商品删除失败");
        }
        postService.deletePosts(postId);
        return R.success(null,"商品删除成功");
    }

}
