package com.example.demo.service;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public  Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findById(Integer number){
        return userRepository.findById(number);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteUser(Integer userId){
        return userRepository.deleteUser(userId);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int recoverUser(Integer userId){
        return userRepository.recoverUser(userId);
    }

    public List<User> findAllUsers(Integer pagenum, Integer pagesize, String query) {
        // 获取商品的总数
        int total = userRepository.countGoods(query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        Integer query1=(pagenum-1)*pagesize;
        return userRepository.findAllUsers(query1, pagesize, query);
    }
}
