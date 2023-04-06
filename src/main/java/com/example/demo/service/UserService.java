package com.example.demo.service;

import com.example.demo.common.R;
import com.example.demo.dao.UserRepository;
import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    public  Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username){ return userRepository.findByUsername(username); }

    public Optional<User> findByPhone(String phone){ return userRepository.findByPhone(phone); }

    public Optional<User> findByNum(String num){ return userRepository.findByNum(num); }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int deleteUser(Integer userId){
        return userRepository.deleteUser(userId);
    }

    @Transactional//UPDATE 或 DELETE 操作需要使用事务，需要在定义的业务逻辑层（Service层），在方法上使用@Transactional注解管理事务。
    public int recoverUser(Integer userId){
        return userRepository.recoverUser(userId);
    }


    public List<User> findAllUsers(Integer pagenum, Integer pagesize) {
        // 获取商品的总数
        int total = getTotalPage();
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }
        pagenum=(pagenum-1)*pagesize;
        return userRepository.findAllUsers(pagenum, pagesize);
    }
    //query不为空时，查询特定字段，使用entityManager 创建本地查询自定义sql
    public List<User> findCertainUsers(Integer pagenum, Integer pagesize, String kind, String query) {
        // 获取商品的总数
        int total = getCertainPage(kind,query);
        // 计算总页数
        int totalPage = total/pagesize + 1;
        // 如果传入参数超出页数范围，则返回空
        if(pagenum > totalPage || pagenum < 1){
            return null;
        }

        pagenum=(pagenum-1)*pagesize;
        String sql="select a.* from user a join user b " +
                "on a.id = b.id " +
                "where a.display=0 and b."+kind+" like '%"+query+"%'"+
                " limit "+pagenum+" , "+pagesize;
        Query query1=entityManager.createNativeQuery(sql);
        query1.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<User> resultList = query1.getResultList();
        return resultList;
    }
    // 按照query获取商品的总数
    public int getTotalPage(){
        return userRepository.countUser();
    }
    public int getCertainPage(String kind,String query){
        String sql="select a.* from user a join user b " +
                "on a.id = b.id " +
                "where a.display=0 and b."+kind+" like '%"+query+"%'";
        Integer total= entityManager.createNativeQuery(sql).getResultList().size();
        return total;
    }
    public Optional<User> addOneUser(String username, String password, String num, String phone, String qq, String addr,String token){
        String uuid=java.util.UUID.randomUUID().toString();
        userRepository.createOneUser(username,password,num,phone,token,uuid,qq,addr);
        return userRepository.findByNum(num);
    }

    //修改用户信息，将原来的用户软删除，然后新建一个用户
    @Transactional
    public Optional<User> updateUser(Integer id, String username, String password, String num, String phone, String token, String qq, String addr){
        userRepository.deleteUser(id);
        String uuid=java.util.UUID.randomUUID().toString();
        userRepository.createOneUser(username,password,num,phone,token,uuid,qq,addr);
        return userRepository.findByNum(num);
    }
    // 插入num个用户
    public void addUsers(int num){
        for(int i = 0; i < num; i++){
            User user = generateOneUser();
            // 调用create2插入
            userRepository.createOneUser2(
                    user.getUsername(), user.getPassword(), user.getNum(), user.getPhone(),
                    user.getQq(), user.getAddr(), user.getBan(),
                    user.getToken(), user.getIcon(), user.getCreate_at(), user.getUpdate_at(),
                    0, user.getUuid());
        }
    }

    // 生成一个随机的user
    public User generateOneUser(){
        // 生成随机的用户名
        String[] surnames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯"
                , "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许"};
        String[] names = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军",
                "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞", "平", "刚",
                "桂英", "桂兰", "桂芳", "桂荣", "桂香", "桂珍", "桂英", "桂华", "桂芝",
                "桂林", "桂花", "桂秀", "桂莲", "桂梅", "桂琴", "桂英", "桂芬", "桂花",
                "桂芳", "桂荣", "桂珍", "桂英", "桂华", "桂芝", "桂林", "桂花", "桂秀",
                "桂莲", "桂梅", "桂琴"};
        // 生成随机的地址
        String[] addrs = {"广东省广州市天河区", "广东省广州市海珠区", "广东省广州市番禺区", "广东省广州市白云区",
                "广东省广州市花都区", "广东省广州市黄埔区", "广东省广州市荔湾区", "广东省广州市越秀区",
                "广东省广州市南沙区", "广东省广州市萝岗区", "北京市东城区", "北京市西城区", "北京市崇文区",
                "北京市宣武区", "北京市朝阳区", "北京市丰台区", "北京市石景山区","上海市黄浦区", "上海市卢湾区",
                "上海市徐汇区", "上海市长宁区", "上海市静安区", "上海市普陀区", "上海市闸北区", "上海市虹口区",
                "天津市和平区", "天津市河东区", "天津市河西区", "天津市南开区", "天津市河北区", "天津市红桥区",
                "天津市东丽区", "天津市西青区", "天津市津南区", "天津市北辰区", "天津市武清区", "天津市宝坻区",
                "天津市滨海新区", "天津市宁河区", "天津市静海区", "天津市蓟州区", "重庆市渝中区", "重庆市大渡口区",
                "重庆市江北区", "重庆市沙坪坝区", "重庆市九龙坡区", "重庆市南岸区", "重庆市北碚区", "重庆市万盛区",
                "重庆市双桥区", "重庆市渝北区", "重庆市巴南区", "重庆市黔江区", "重庆市长寿区", "重庆市綦江区",
                "重庆市潼南区", "重庆市铜梁区", "重庆市大足区", "重庆市荣昌区", "重庆市璧山区", "重庆市梁平区",
                "重庆市城口县", "重庆市丰都县", "重庆市垫江县", "重庆市武隆县", "重庆市忠县", "重庆市开县"};
        User user = new User();
        // 设置用户名
        user.setUsername(surnames[(int)(Math.random()*20)] + names[(int)(Math.random()*50)]);
        // 设置密码
        String password = UUID.randomUUID().toString();
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        // 设置学号
        StringBuilder num = new StringBuilder();
        for(int i = 0; i < 10; i++)
            num.append((int) (Math.random() * 10));
        user.setNum(String.valueOf(num));
        // 设置手机号（11位数字字符串）
        StringBuilder phone = new StringBuilder();
        for(int i = 0; i < 11; i++)
            phone.append((int) (Math.random() * 10));
        user.setPhone(phone.toString());
        // 设置qq
        StringBuilder qq = new StringBuilder();
        for(int i = 0; i < 10; i++)
            qq.append((int) (Math.random() * 10));
        user.setQq(String.valueOf(qq));
        // 设置地址
        user.setAddr(addrs[(int)(Math.random()*50)]);
        // 设置禁用
        user.setBan(0);
        // 设置token
        user.setToken(DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()));
        // 设置icon
        user.setIcon("default.jpg");
        // 设置注册时间
        user.setCreate_at(new Date());
        // 设置最后登录时间
        user.setUpdate_at(new Date());
        user.setDisplay(Boolean.FALSE);
        user.setUuid(UUID.randomUUID().toString());
        return user;
    }
}
