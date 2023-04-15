package com.example.demo.dao;


import com.example.demo.entity.Report;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReportRepository extends CrudRepository<Report, String> {

    @Query(value="select * from report where id = ?1" ,nativeQuery=true)
    Optional<Report> findById(Integer id);

    @Query(value="select r.*,u1.username,u1.phone,u1.num,u2.username as reporter " +
            "from report r join user u1 on r.uid1=u1.id " +
            "join user u2 on r.uid2=u2.id "+
            "where r.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String,Object>> findAllReports(Integer pagenum, Integer pagesize);

    @Query(value="select count(*) from report where report.display = 0" ,nativeQuery=true)
    int countReports();

    @Modifying
    @Query(value="update report set report.display = 1 where id = ?1" ,nativeQuery=true)
    int deleteReport(Integer reportId);

}
