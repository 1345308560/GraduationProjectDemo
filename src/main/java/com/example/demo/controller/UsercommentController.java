package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.entity.Usercomment;
import com.example.demo.service.UserService;
import com.example.demo.service.UsercommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(path="/user_comment")
public class UsercommentController {

    @Autowired
    UsercommentService usercommentService;

    @Autowired
    UserService userService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>>> getAllComment(@RequestParam Map<String,Object> map){
        Integer pagenum= Integer.valueOf((String) map.get("pagenum"));
        Integer pagesize= Integer.valueOf((String) map.get("pagesize"));
        String query= map.get("query").toString();
        String kind= map.get("kind").toString();
        if (pagenum == null){
            pagenum=1;
        }
        if (pagesize == null){
            pagesize=10;
        }
        if(Objects.equals(query, "")){
            // 获取总页数
            Integer total=usercommentService.getTotalPage();
            log.info("query为空");
            return R.success(usercommentService.findAllComment(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            String querykind=null;

            if(Objects.equals(kind, "username")){
                querykind="uid1";
            }else {
                querykind="uid2";
            }

            Integer total = usercommentService.getCertainPage(querykind, query);
            return R.success(usercommentService.findCertainUsersComment(pagenum, pagesize, querykind, query)).add("total", total);
        }
    }

    @PutMapping(path = "/delete")
    public @ResponseBody R deleteUserComment(@RequestParam Map<String,Object> head){
        Integer commentId= Integer.valueOf((String) head.get("id"));
        if(!usercommentService.findById(commentId).isPresent()){
            return R.error("用户删除失败");
        }
        usercommentService.deleteComment(commentId);
        return R.success(null,"用户删除成功");
    }
}
