package com.example.demo.service;

import com.example.demo.dao.ReportCountRepository;
import com.example.demo.dao.ReportRepository;
import com.example.demo.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ReportCountRepository reportCountRepository;

    public List<Map<String,Object>> findAllReports(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countReports();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        List<Map<String,Object>> res=reportRepository.findAllReports(pagenum, pagesize);
        return res;
    }

    public int countReports(){
        return reportRepository.countReports();
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteReport(Integer reportId) {
        return reportRepository.deleteReport(reportId);
    }

    public Optional<Report> findById(Integer reportId) {
        return reportRepository.findById(reportId);
    }

    public int countReportsTotal() {
        return reportCountRepository.countReportsTotal();
    }

    public List<Map<String,Object>> findReportsTotal(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countReportsTotal();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        List<Map<String,Object>> res=reportCountRepository.findReportsTotal(pagenum, pagesize);
        return res;
    }
}
