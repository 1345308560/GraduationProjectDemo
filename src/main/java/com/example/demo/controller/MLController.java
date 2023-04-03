package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "ml")
public class MLController {
    @GetMapping(path="/predict")
    public @ResponseBody String predict(String input_str) {

        // 向http://ml:5000/predict发送input_str
        // 发送get请求，并返回返回值
        String url = "http://127.0.0.1:5000/predict?input_str=" + input_str;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
