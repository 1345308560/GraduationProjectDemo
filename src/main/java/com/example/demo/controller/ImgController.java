package com.example.demo.controller;


import com.example.demo.common.FileUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Controller
@RequestMapping(path="/imgs")
public class ImgController {


    @Resource
    HttpServletRequest request;

    //读取图片
    @GetMapping("/loadimg/**")
    public void getImg2(HttpServletResponse response, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI=request.getRequestURI();
        String img = StringUtils.substringAfter(requestURI,"loadimg/");
        //String imgPath="E:/usr/img/goods/";
        String imgPath="/usr/img/goods/";
        String url = imgPath+img;
        File file = new File(url);//imgPath为服务器图片地址

        if(file.exists() && file.isFile()){
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //处理文件上传
    @PostMapping("/upload/**")
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file,@RequestParam Map map) {
        String fileName = UUID.randomUUID().toString()+".jpg";
        log.info("{}",fileName);
        //设置文件上传路径
        //String filePath ="E:/usr/img/goods/";
        String filePath ="/usr/img/goods/";
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            return "上传成功";
        } catch (Exception e) {
            return "上传失败";
        }
    }
}
