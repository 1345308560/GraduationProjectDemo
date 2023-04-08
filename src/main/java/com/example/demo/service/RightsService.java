package com.example.demo.service;

import com.example.demo.dao.RightsRepository;
import com.example.demo.entity.Rights;
import com.example.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RightsService {

    @Autowired
    RightsRepository rightsRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager entityManager;

    public Optional<Rights> findByUid(String uid){ return rightsRepository.findByUid(uid); }

    public List<Map<String,Object>>  findAllUsersRights(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = userService.getTotalPage();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        pagenum=(pagenum-1)*pagesize;
        return rightsRepository.findAllUsersRights(pagenum, pagesize);
    }

    //query不为空时，查询特定字段，使用entityManager 创建本地查询自定义sql
    public List<Map<String,Object>> findCertainUsersRights(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = userService.getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }

        pagenum=(pagenum-1)*pagesize;
        String sql="select u.*,r.rights_buy,r.rights_sell from user u join rights r " +
                "on u.num = r.uid " +
                "where u.display=0 and u."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }

    public void createOneUserRights(String num,Integer rights_buy,Integer rights_sell){
        rightsRepository.createOneUserRights(num,rights_buy,rights_sell);
        return;
    }
}
