package com.example.demo.controller;

import com.example.demo.common.BaseContext;
import com.example.demo.common.R;
import com.example.demo.entity.Admin;
import com.example.demo.entity.User;
import com.example.demo.service.RightsService;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(path="/user")
public class UserController {
    // Set方法注入
    @Autowired
    private UserService userService;

    @Autowired
    private RightsService rightsService;


    @PutMapping(path = "/delete")
    public @ResponseBody R deleteUser(@RequestParam Map<String,Object> head){
        Integer userId= Integer.valueOf((String) head.get("id"));
        if(!userService.findById(userId).isPresent()){
            return R.error("用户删除失败");
        }
        userService.deleteUser(userId);
        return R.success(null,"用户删除成功");
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
    public @ResponseBody R<List<User>> getAllUsers(@RequestParam Map<String,Object> map){
        Integer pagenum= Integer.valueOf((String) map.get("pagenum"));
        Integer pagesize= Integer.valueOf((String) map.get("pagesize"));
        String query= map.get("query").toString();
        String kind= map.get("kind").toString();
        if (pagenum == null){
            pagenum=1;
        }
        if (pagesize == null){
            pagesize=10;
        }
        if(Objects.equals(query, "")){
            // 获取总页数
            Integer total=userService.getTotalPage();
            log.info("query为空");
            return R.success(userService.findAllUsers(pagenum,pagesize)).add("total",total);
        }else {
            log.info("query不为空");
            // 获取总页数
            Integer total = userService.getCertainPage(kind, query);
            return R.success(userService.findCertainUsers(pagenum, pagesize, kind, query)).add("total", total);
        }
    }

    @PostMapping(path = "/add")
        public @ResponseBody R<Optional<User>> addUser(@RequestBody Map map){
        String username=map.get("username").toString();
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        String qq=map.get("qq").toString();
        String addr=map.get("addr").toString();

        if(userService.findByPhone(phone).isPresent()){
            return R.error("手机号已被注册");
        }
        if(userService.findByNum(num).isPresent()){
            return R.error("学号已被注册");
        }
        if(userService.findByUsername(username).isPresent()){
            return R.error("用户名已被注册");
        }
        if(userService.findByQQ(qq).isPresent()){
            return R.error("qq已被注册");
        }
        if(qq==""){
            qq=null;
        }
        String token = DigestUtils.md5DigestAsHex(num.getBytes(StandardCharsets.UTF_8));
        log.info("token:{}",token);
        Optional<User> newUser=userService.addOneUser(username,password,num,phone,qq,addr,token);


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

    @PutMapping(path = "")
    public @ResponseBody R<Optional<User>> updateUser(@RequestParam Map<String,Object> head,@RequestBody Map map){
        Integer id= Integer.valueOf((String) head.get("id"));
        String username=map.get("username").toString();
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        String qq=map.get("qq").toString();
        String addr=map.get("addr").toString();
        if(username =="" || num =="" || phone=="" ||password=="" || addr=="" || qq==""){
            return R.error("信息不完全");
        }
        if(userService.findByPhone(phone).isPresent()){
            if(!userService.findByPhone(phone).get().getId().equals(id)){
                return R.error("手机号已被注册");
            }
        }
        Optional<User>  user=userService.updateUser(id, username, password, num, phone, qq, addr);

        return R.success(user,"修改成功");
    }

    @PutMapping(path = "/buy_power/**")
    public @ResponseBody R updateBuyRights(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url=request.getRequestURI();
        log.info("{}",url);
        String id= StringUtils.substringBetween(url,"buy_power/","/state");
        String type_ = StringUtils.substringAfter(url,"state/");
        Integer uid= Integer.valueOf(id);
        Boolean type= Boolean.valueOf(type_);
        if(type==true){
            userService.updateRights_buy1(uid);
            return R.success(null,"修改成功");
        }
        if(type==false){
            userService.updateRights_buy0(uid);
            return R.success(null,"修改成功");
        }

        return R.error("error");
    }

    @PutMapping(path = "/sale_power/**")
    public @ResponseBody R updateSellRights(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url=request.getRequestURI();
        log.info("{}",url);
        String id= StringUtils.substringBetween(url,"sale_power/","/state");
        String type_ = StringUtils.substringAfter(url,"state/");
        Integer uid= Integer.valueOf(id);
        Boolean type= Boolean.valueOf(type_);
        if(type==true){
            userService.updateRights_sell1(uid);
            return R.success(null,"修改成功");
        }
        if(type==false){
            userService.updateRights_sell0(uid);
            return R.success(null,"修改成功");
        }

        return R.error("error");
    }

    //读取图片
    @GetMapping(path = "/front/loadimg/**")
    public void getIcon(HttpServletResponse response, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI=request.getRequestURI();
        String img = StringUtils.substringAfter(requestURI,"loadimg/");
        String imgPath="E:/usr/img/icon/";
        String url = imgPath+img;
        log.info("{}",url);
        File file = new File(url);//imgPath为服务器图片地址

        if(file.exists() && file.isFile()){
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @PutMapping(path = "/ban_power/**")
    public @ResponseBody R banUser(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url=request.getRequestURI();
        log.info("{}",url);
        //http://localhost:8080/user/ban_power/402/state/true
        String id= StringUtils.substringBetween(url,"ban_power/","/state");
        String type_ = StringUtils.substringAfter(url,"state/");
        Integer uid= Integer.valueOf(id);
        Boolean type= Boolean.valueOf(type_);
        if(type==true){
            userService.banUser(uid);
            return R.success(null,"修改成功");
        }
        if(type==false){
            userService.recoverUser(uid);
            return R.success(null,"修改成功");
        }

        return R.error("error");
    }

    //前台用户登录
    @PostMapping (path = "/front/login")
    public @ResponseBody R<User> login(@RequestBody Map<String,Object> map){
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        if(username.length()==10){
            //如果前端发送的账号是10位  那么使用学号进行登录
            if(userService.findByNum(username).isEmpty()){
                return R.error("用户名或密码错误");
            }
            User user=userService.findByNum(username).get();
            if(user.getPassword()!=null&&user.getPassword().equals(password)){
                return R.success(user);
            }
            return R.error("用户名或密码错误");
        }
        //否则使用手机号进行登录
        if(userService.findByPhone(username).isEmpty()){
            return R.error("用户名或密码错误");
        }
        User user=userService.findByPhone(username).get();
        if(user.getPassword()!=null&&user.getPassword().equals(password)){
            return R.success(user);
        }
        return R.error("用户名或密码错误");
    }

    //用户注册
    @PostMapping(path = "/front/add")
    public @ResponseBody R<Optional<User>> userRegist(@RequestBody Map map){
        String username=map.get("username").toString();
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        String qq=map.get("qq").toString();
        String addr=map.get("addr").toString();
        if(username =="" || num =="" || phone=="" ||password=="" || addr=="" || qq==""){
            return R.error("信息不完全");
        }
        if(userService.findByPhone(phone).isPresent()){
            return R.error("手机号已被注册");
        }
        if(userService.findByNum(num).isPresent()){
            return R.error("学号已被注册");
        }
        if(userService.findByUsername(username).isPresent()){
            return R.error("用户名已被注册");
        }
        if(userService.findByQQ(qq).isPresent()){
            return R.error("qq已被注册");
        }
        String token = DigestUtils.md5DigestAsHex(num.getBytes(StandardCharsets.UTF_8));
        log.info("token:{}",token);
        Optional<User> newUser=userService.addOneUser(username,password,num,phone,qq,addr,token);


        return R.success(newUser);
    }

    //用户个人信息
    @GetMapping(path="/front/message")
    public @ResponseBody R<Optional<User>> userMessage(ServletRequest servletRequest){
        Integer userId= BaseContext.getCurrentId();
        Optional<User> user=userService.findById(userId);
        return R.success(user);
    }

    //用户修改个人信息
    @PutMapping(path = "/front/changeSelf")
    public @ResponseBody R<Optional<User>> updateUserSelf( @RequestBody Map map){
        Integer id= BaseContext.getCurrentId();
        String username=map.get("username").toString();
        String num=map.get("num").toString();
        String phone=map.get("phone").toString();
        String password=map.get("password").toString();
        String qq=map.get("qq").toString();
        String addr=map.get("addr").toString();
        if(username =="" || num =="" || phone=="" ||password=="" || addr=="" || qq==""){
            return R.error("信息不完全");
        }
        if(userService.findByPhone(phone).isPresent()){
            if(!userService.findByPhone(phone).get().getId().equals(id)) {
                return R.error("手机号已被注册");
            }
        }
        Optional<User>  user=userService.updateUser(id, username, password, num, phone, qq, addr);

        return R.success(user,"修改成功");
    }

    @PutMapping(path = "/front/changeSelfIcon")
    public @ResponseBody R updateUserIcon(@RequestBody Map map){
        Integer userId=BaseContext.getCurrentId();
        String userIcon=map.get("icon").toString();
        int res=userService.updateUserIcon(userId,userIcon);

        return R.success(res,"修改成功");
    }


}

