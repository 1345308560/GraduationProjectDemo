package com.example.demo.service;

import com.example.demo.dao.AdminRepository;

import java.time.Instant;
import java.util.Random;
import com.example.demo.entity.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AdminService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * 创建num数量的管理员信息
     * @param num 创建的管理员数量
     */
    public void createAdminByNum(Integer num){
        for(int i=0;i<num;i++){
            Admin admin = randomOneAdmin();
            adminRepository.createOneAdmin( admin.getUsername(),
                    admin.getPassword(),admin.getNum(),
                    admin.getPhone(),
                    admin.getToken(),admin.getCreate_at(),
                    admin.getUpdate_at(),admin.getDisplay(),
                    admin.getUuid());
        }
    }
    public  Iterable<Admin> findAll() {
        return adminRepository.findAll();
    }
    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> findById(Integer id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> findByPhone(String phone) { return adminRepository.findByPhone(phone); }

    public Optional<Admin> findByNum(String num) {
        return adminRepository.findByNum(num);
    }

    public Optional<Admin> findByToken(String token){ return adminRepository.findByToken(token); }

    /**
     * 创建一个随机Admin
     * @return admin
     */
    private Admin randomOneAdmin(){
        String[] surnames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许"};
        String[] names = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞", "平", "刚", "桂英", "桂兰", "桂芳", "桂荣", "桂香", "桂珍", "桂英", "桂华", "桂芝", "桂林", "桂花", "桂秀", "桂莲", "桂梅", "桂琴", "桂英", "桂芬", "桂花", "桂芳", "桂荣", "桂珍", "桂英", "桂华", "桂芝", "桂林", "桂花", "桂秀", "桂莲", "桂梅", "桂琴"};
        Admin admin = new Admin();
        admin.setId((int)(Math.random()*100000));
        admin.setUsername(surnames[(int)(Math.random()*20)] + names[(int)(Math.random()*50)]);
        String password = UUID.randomUUID().toString();
        admin.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        admin.setNum(String.valueOf((long)(Math.random()*9000000000L)+1000000000L));
        // 生成11位数的以1开头的随机手机号
        admin.setPhone("1"+String.valueOf((long)(Math.random()*9000000000L)+1000000000L));
        admin.setToken(DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()));
        Date startDate = Date.from(Instant.parse("2022-01-01T00:00:00.00Z"));
        Date endDate = new Date();
        long randomTime = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
        Date randomDate = new Date(randomTime);
        admin.setCreate_at(randomDate);
        long randomTime2 = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());
        Date randomDate2 = new Date(randomTime2);
        admin.setUpdate_at(randomDate2);
        admin.setDisplay(0);
        admin.setUuid(UUID.randomUUID().toString());
        return admin;
    }

    @Transactional
    public Optional<Admin> updateAdmin(Integer id, String username, String password, String num, String phone) {
        String sql="update admin set admin.username='"+username+
                "',admin.password='"+password+
                "',admin.num='"+num+
                "',admin.phone='"+phone+
                "' where admin.id="+id;
        Integer col = entityManager.createNativeQuery(sql).executeUpdate();
        return adminRepository.findByNum(num);
    }
}
