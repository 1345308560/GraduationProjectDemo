package com.example.demo.controller;


import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Admin> getAllAdmins() {
        // This returns a JSON or XML with the users
        return adminService.findAll();
    }

    @PostMapping (path = "/login")
    public @ResponseBody Optional<Admin> getById(String id){
        return adminService.findById(id);
    }
}
