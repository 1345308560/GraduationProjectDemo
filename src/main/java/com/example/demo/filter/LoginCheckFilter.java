package com.example.demo.filter;

//import com.alibaba.fastjson.JSON;

import com.example.demo.common.R;
import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import java.io.IOException;
import java.util.Optional;


/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Autowired
    private AdminService adminService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        String method=request.getMethod();
        String authorization=request.getHeader("Authorization");
        log.info("拦截到请求：{}",requestURI);
        log.info("请求方法：{}",method);
        log.info("请求头部：{}",authorization);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/admin/login",
                "/admin/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/favicon.ico",
                "/ml/**",             // 机器学习接口，测试使用，正式上线时需要删除
                "/user/generate"      // 生成随机用户，测试使用，正式上线时需要删除
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.1如果请求方式为options，则直接放行
        if(method.equals("OPTIONS")){
            log.info("本次请求{}方式为options",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //3.2如果请求不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1、判断登录状态，如果已登录，则直接放行
        if(authorization!= null){
            Optional<Admin> admin=adminService.findByToken(authorization);
            if(admin.isPresent()){
                log.info("用户已登录，username为：{}",admin.get().getUsername());
                filterChain.doFilter(request,response);
                return;
            }
            log.info("用户不存在!");
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        //response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}