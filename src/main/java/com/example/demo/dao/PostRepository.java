package com.example.demo.dao;

import com.example.demo.entity.Goods;
import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository  extends CrudRepository<Post, String> {

    @Query(value="select * from post where id = ?1" ,nativeQuery=true)
    Optional<Post> findById(Integer id);
    @Query(value="select count(*) from post where display = 0" ,nativeQuery=true)
    int countPost();

    @Query(value="select p.*,u.username,u.num,s.sname " +
            "from post p join user u " +
            "on p.uid=u.id " +
            "join section s "+
            "on p.sid=s.sid " +
            "where p.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String, Object>> findAllPost(Integer pagenum, Integer pagesize);

    @Modifying
    @Query(value="update post set display = 1 where id = ?1" ,nativeQuery=true)
    int deletePost(Integer postId);
}
