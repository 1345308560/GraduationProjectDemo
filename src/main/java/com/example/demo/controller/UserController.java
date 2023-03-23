package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.dao.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}

