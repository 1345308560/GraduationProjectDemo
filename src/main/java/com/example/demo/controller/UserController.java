package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
    public @ResponseBody R deleteUser(@RequestBody Map map){
        Integer userId= (Integer) map.get("id");
        userService.deleteUser(userId);
        if(!userService.findById(userId).isPresent()){
            return R.error("用户删除失败");
        }
        User user=userService.findById(userId).get();
        if (user.getDisplay()==true){
            return R.success(null,"用户删除成功");
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
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();

        if(userService.findByPhone(phone).isPresent()){
            return R.error("手机号已被注册");
        }
        if(userService.findByNum(num).isPresent()){
            return R.error("学号已被注册");
        }
        if(userService.findByUsername(username).isPresent()){
            return R.error("用户名已被注册");
        }
        String token = DigestUtils.md5DigestAsHex(num.getBytes(StandardCharsets.UTF_8));
        log.info("token:{}",token);
        Optional<User> newUser=userService.addOneUser(username,password,num,phone,token);

        return R.success(newUser);
    }
    /**
     * Get请求：
     * 请求接口：/user/generate
     * 携带数据：num:创建用户数量
     * 返回值为num
     */
    @GetMapping(path = "/generate")
    public @ResponseBody  String generateUser(@RequestParam Map map){
        int num= Integer.parseInt((String) map.get("num"));
        userService.addUsers(num);
        return Integer.toString(num);
    }
}

