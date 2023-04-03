package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Admin> getAllAdmins() {
        // This returns a JSON or XML with the admins
        return adminService.findAll();
    }
    @PostMapping(path ="/create")
    public @ResponseBody String createAdmin(Integer num){
        adminService.createAdminByNum(num);
        return num.toString();
    }
    @PostMapping (path = "/login")
    public @ResponseBody R<Admin> login(@RequestBody Map<String,Object> map){
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        if(username.length()==10){
            //如果前端发送的账号是10位  那么使用学号进行登录
            if(adminService.findByNum(username).isEmpty()){
                return R.error("用户名或密码错误");
            }
            Admin admin=adminService.findByNum(username).get();
            if(admin.getPassword()!=null&&admin.getPassword().equals(password)){
                return R.success(admin);
            }
            return R.error("用户名或密码错误");
        }
        //否则使用手机号进行登录
        if(adminService.findByPhone(username).isEmpty()){
            return R.error("用户名或密码错误");
        }
        Admin admin=adminService.findByPhone(username).get();
        if(admin.getPassword()!=null&&admin.getPassword().equals(password)){
            return R.success(admin);
        }
        return R.error("用户名或密码错误");
    }

}
