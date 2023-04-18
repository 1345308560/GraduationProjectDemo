package com.example.demo.service;

import com.example.demo.dao.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    PostRepository postRepository;

    public int countPost() {
        return postRepository.countPost();
    }

    public List<Map<String,Object>> findAllPost(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = countPost();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        // 计算偏移量
        pagenum=(pagenum-1)*pagesize;
        return postRepository.findAllPost(pagenum, pagesize);
    }

    public Integer getCertainPage(String kind, String query) {
        String table=null;
        if(kind.equals("username")) {
            table="u";
        }
        else{
            table="p";
        }
        String sql="select count(*)  from post p join user u " +
                "on p.uid = u.id where p.display=0 and "+table+"."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

    public List<Map<String,Object>> findCertainPost(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        String table=null;
        if(kind.equals("username")) {
            table="u";
        }
        else{
            table="p";
        }
        pagenum=(pagenum-1)*pagesize;
        String sql="select p.*,u.username,u.num from post p join user u " +
                "on p.uid = u.id " +
                "where p.display=0 and "+ table+"."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> resultList = query1.getResultList();
        return resultList;
    }
}
