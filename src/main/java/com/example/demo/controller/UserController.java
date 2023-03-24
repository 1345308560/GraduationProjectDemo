package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.dao.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path="/demo")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String id) {
        User n = new User();
        n.setName(name);
        n.setId(id);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

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
}

