package com.example.demo.service;

import com.example.demo.dao.StudentRepository;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service处理所有的业务逻辑
 */
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public  Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(int id){
        return studentRepository.findById(id);
    }
}
