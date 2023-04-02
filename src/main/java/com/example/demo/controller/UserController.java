package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.Goods;
import com.example.demo.entity.User;
import com.example.demo.dao.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping(path = "/delete")
    public @ResponseBody R<User> deleteUser(@RequestBody Map map){
        Integer userId= (Integer) map.get("id");
        if(!userService.findById(userId).isPresent()){
            return R.error("用户删除失败");
        }
        userService.deleteUser(userId);
        User user=userService.findById(userId).get();
        if (user.getDisplay()==true){
            return R.success(user);
        }
        return R.error("用户删除失败");
    }

    /**
     * Get请求：
     * 请求接口：/user/all
     * 携带数据：queryInfo: {
     *                 query: '',//查询参数（比如账号，电话）
     *                 pagenum: 1,//当前页码值
     *                 pagesize: 10//每页显示条数
     *             }
     * （查询参数如果为空那么视为全部数据，当前页码值为空则为1，显示条数非空）
     * 返回值就为查询的那几条用户数据，
     * 如果下一页有数据返回状态200，message：查询成功
     * 下一页没有数据返回其他状态值，message：无更多数据
     */
    @GetMapping(path = "/all")
    public @ResponseBody R<List<User>> getAllUsers(@RequestParam Map map){
        Integer pagenum= Integer.valueOf((String) map.get("pagenum"));
        Integer pagesize= Integer.valueOf((String) map.get("pagesize"));
        String query= (String) map.get("query");
        if (pagenum==null){
            pagenum=1;
        }
        if (pagesize==null){
            pagesize=10;
        }
        if (query==null){
            query="";
        }
        log.info("pagenum:{}",pagenum);
        log.info("pagesize:{}",pagesize);
        return R.success(userService.findAllUsers(pagenum,pagesize,query));
    }

    @PostMapping(path = "/add")
    public @ResponseBody R<Optional<User>> addUser(@RequestBody Map map){
        String username=map.get("username").toString();
        String num=map.get("number").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        String token=null;

        if(userService.findByPhone(phone).isPresent()){
            return R.error("手机号已被注册");
        }
        if(userService.findByNum(num).isPresent()){
            return R.error("学号已被注册");
        }
        if(userService.findByUsername(username).isPresent()){
            return R.error("用户名已被注册");
        }

        Optional<User> newUser=userService.addOneUser(username,password,num,phone,token);

        return R.success(newUser);
    }
}

