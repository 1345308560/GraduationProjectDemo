package com.example.demo.dao;


import com.example.demo.entity.Usercomment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsercommentRepository extends CrudRepository<Usercomment, String> {

    @Query(value="select * from usercomment where id = ?1" ,nativeQuery=true)
    Optional<Usercomment> findById(Integer id);

    @Modifying
    @Query(value="update usercomment set usercomment.display = 1 where id = ?1" ,nativeQuery=true)
    int deleteComment(Integer id);//软删除，修改display即可

    @Query(value="select count(*) from usercomment where display = 0" ,nativeQuery=true)
    int countComment();

    @Query(value="select * from usercomment where display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Usercomment> findAllComment(Integer pagenum, Integer pagesize);

}
