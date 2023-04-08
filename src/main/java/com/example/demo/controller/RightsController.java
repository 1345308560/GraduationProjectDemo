package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.service.RightsService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping(path = "power")
public class RightsController {

    @Autowired
    RightsService rightsService;

    @Autowired
    UserService userService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>> > getAllRights(@RequestParam Map<String,Object> map){
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
            Integer total=userService.getTotalPage();
            log.info("query为空");
            return R.success(rightsService.findAllUsersRights(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            Integer total = userService.getCertainPage(kind, query);
            return R.success(rightsService.findCertainUsersRights(pagenum, pagesize, kind, query)).add("total", total);
        }
    }

}
