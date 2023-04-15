package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(path = "inform")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping(path = "/all")
    public @ResponseBody R<List<Map<String,Object>>> getAllReports(@RequestParam Map<String,Object> map){
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
            Integer total=reportService.countReports();
            log.info("query为空");
            return R.success(reportService.findAllReports(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            //Integer total = goodsService.getCertainPage(kind, query);
            //return R.success(goodsService.findCertainGoods(pagenum, pagesize, kind, query)).add("total", total);
            return R.error("?");
        }
    }

    @PutMapping(path = "/delete")
    public @ResponseBody R<Goods> deleteReport(@RequestParam Map<String,Object> head){
        Integer reportId= Integer.valueOf((String) head.get("id"));
        if(!reportService.findById(reportId).isPresent()){
            return R.error("举报删除失败");
        }
        reportService.deleteReport(reportId);
        return R.success(null,"举报删除成功");

    }
}
