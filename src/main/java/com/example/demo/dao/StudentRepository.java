package com.example.demo.dao;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 持久层直接对数据库进行操作
 */
public interface StudentRepository extends CrudRepository<Student, Integer> {

    @Override
    @Query("SELECT stu FROM Student stu")
    Iterable<Student> findAll();

    // 使用原生SQL语句查询
    @Query(value="select * from student where id = ?1" ,nativeQuery=true)
    Student findById(int id);
}
