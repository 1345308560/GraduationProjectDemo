package com.example.demo.dao;

import com.example.demo.entity.Report_count;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ReportCountRepository extends CrudRepository<Report_count, String> {

    @Query(value="select count(*) from report_count where report_count.display = 0" ,nativeQuery=true)
    int countReportsTotal();


    @Query(value="select rc.*,u.username,u.phone,u.num " +
            "from report_count rc join user u on rc.uid=u.id " +
            "where rc.display = 0 limit ?1, ?2" ,nativeQuery=true)
    List<Map<String, Object>> findReportsTotal(Integer pagenum, Integer pagesize);
}
