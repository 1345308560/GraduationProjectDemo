package com.example.demo.controller;


import com.example.demo.common.BaseContext;
import com.example.demo.common.R;
import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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

    @GetMapping(path="/message")
    public @ResponseBody R<Optional<Admin>> adminMessage(){
        Integer adminId= BaseContext.getCurrentId();
        Optional<Admin> admin=adminService.findById(adminId);

        return R.success(admin);
    }

    @PutMapping(path = "")
    public @ResponseBody R<Optional<Admin>> updateAdmin(@RequestParam Map<String,Object> head, @RequestBody Map map){
        Integer id= Integer.valueOf((String) head.get("id"));
        String username=map.get("username").toString();
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        if(username =="" || num =="" || phone=="" ||password==""){
            return R.error("信息不完全");
        }
        if(adminService.findByPhone(phone).isPresent()){
            if(adminService.findByPhone(phone).get().getId()!=id) {
                return R.error("手机号已被注册");
            }
        }
        //String token = DigestUtils.md5DigestAsHex(num.getBytes(StandardCharsets.UTF_8));
        Optional<Admin>  admin=adminService.updateAdmin(id, username, password, num, phone);

        return R.success(admin,"修改成功");
    }

}
