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

@Slf4j
@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping(path = "/updateState")
    public @ResponseBody R<User> updateUserState(@RequestBody Map map){
        Integer userNumber= (Integer) map.get("number");
        Boolean userDisplay=(Boolean) map.get("display");
        if(!userService.findById(userNumber).isPresent()){
            return R.error("用户不存在");
        }
        User user=userService.findById(userNumber).get();
        if (user.getDisplay()!=userDisplay){
            return R.success(user);
        }
        if(userDisplay==false){
            userService.deleteUser(userNumber);
            User userResult=userService.findById(userNumber).get();
            return R.success(userResult);
        }
        userService.recoverUser(userNumber);
        User userResult=userService.findById(userNumber).get();
        return R.success(userResult);
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
}

