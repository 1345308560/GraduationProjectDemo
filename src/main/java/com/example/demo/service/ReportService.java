package com.example.demo.service;

import com.example.demo.dao.ReportCountRepository;
import com.example.demo.dao.ReportRepository;
import com.example.demo.entity.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {
    @PersistenceContext
    EntityManager entityManager;

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

    public int getCertainPage(String kind, String query) {
        String table=null;
        if(kind.equals("reporter")) {
            table="u2";
        }
        else{
            table="u1";
        }
        String sql="select r.*,u1.username,u1.phone,u1.num,u2.username as reporter  " +
                "from report r join user u1 on r.uid1 = u1.id" +
                "join user u2 on r.uid2 = u2.id"+
                "where r.display=0 and "+table+"."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

    public List<Map<String,Object>> findCertainReports(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        String table=null;
        if(kind.equals("reporter")) {
            table="u2";
        }
        else{
            table="u1";
        }
        pagenum=(pagenum-1)*pagesize;
        String sql="select r.*,u1.username,u1.phone,u1.num,u2.username as reporter  " +
                "from report r join user u1 on r.uid1 = u1.id" +
                "join user u2 on r.uid2 = u2.id"+
                "where r.display=0 and "+table+"."+kind+" like '%"+query+"%'" +
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }
}
