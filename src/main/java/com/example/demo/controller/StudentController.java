package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller主要控制所有的接口，不涉及业务处理的信息
 */

@Controller
@RequestMapping(path="/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // GET请求，获取所有的Student信息
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getAllStudent() {
        return studentService.findAll();
    }

    // POST，根据id查询单个student
    @PostMapping(path = "/test")
    public @ResponseBody Student findStudentById(int id){
        return studentService.findById(id);
    }
}
