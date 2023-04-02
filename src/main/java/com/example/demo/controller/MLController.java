package com.example.demo.controller;

import com.example.demo.entity.Admin;
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

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:5000/predict";

        Map<String, String> params = new HashMap<>();
        params.put("input_str", input_str);
        String result = restTemplate.getForObject(url, String.class, params);

        return input_str;
    }
}
