package com.example.demo.controller;


import com.example.demo.common.R;
import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
    public @ResponseBody R<Admin> getById(@RequestBody Map map){
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        if(!adminService.findById(username).isPresent()){
            return R.error("用户名或密码错误");
        }
        Admin admin=adminService.findById(username).get();
        if(admin.getPassword()!=null&&admin.getPassword().equals(password)){
            return R.success(admin);
        }
        return R.error("用户名或密码错误");
    }
}
