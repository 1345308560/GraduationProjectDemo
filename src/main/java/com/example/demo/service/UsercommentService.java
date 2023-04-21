package com.example.demo.service;

import com.example.demo.dao.UsercommentRepository;
import com.example.demo.entity.User;
import com.example.demo.entity.Usercomment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsercommentService {

    @Autowired
    UsercommentRepository usercommentRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Optional<Usercomment> findById(Integer id) {
        return usercommentRepository.findById(id);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteComment(Integer userId){
        return usercommentRepository.deleteComment(userId);
    }

    public List<Usercomment> findAllComment(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = getTotalPage();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        pagenum=(pagenum-1)*pagesize;
        return usercommentRepository.findAllComment(pagenum, pagesize);
    }

    public List<Usercomment> findCertainUsersComment(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }

        pagenum=(pagenum-1)*pagesize;
        String sql="select a.* from usercomment a join usercomment b " +
                "on a.id = b.id " +
                "where a.display=0 and b."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Usercomment> resultList = query1.getResultList();
        return resultList;
    }

    public int getTotalPage(){
            return usercommentRepository.countComment();
    }

    public int getCertainPage(String kind,String query){
        String sql="select a.* from usercomment a join usercomment b " +
                "on a.id = b.id " +
                "where a.display=0 and b."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }

}
