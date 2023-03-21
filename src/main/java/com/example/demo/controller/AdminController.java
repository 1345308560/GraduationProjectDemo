package com.example.demo.controller;

import com.example.demo.dao.Admin;
import com.example.demo.mapper.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Admin> getAllAdmins() {
        // This returns a JSON or XML with the users
        return adminRepository.findAll();
    }
}
