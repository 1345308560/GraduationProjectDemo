package com.example.demo.controller;


import com.example.demo.entity.Goods;
import com.example.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Goods> getAllAdmins() {
        // This returns a JSON or XML with the users
        return goodsService.findAll();
    }
}
