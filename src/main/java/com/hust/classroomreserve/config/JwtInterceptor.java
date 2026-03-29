package com.hust.classroomreserve.config;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.classroomreserve.utils.JwtUtils;

/**
 * Jwt 拦截器
 */
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行拦截
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理请求的对象
     * @return 如果验证通过，则返回true，否则返回false
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 初始化一个Map用于存放验证结果
        Map<String, Object> map = new HashMap<>();
        //获取请求头中的令牌
        String token = request.getHeader("token");
        try {
            // 验证令牌
            JwtUtils.verify(token);
            // 验证通过，继续执行请求
            return true;
        } catch (SignatureVerificationException e) {
            // 捕获无效签名异常
            e.printStackTrace();
            map.put("msg", "无效签名！");
        } catch (TokenExpiredException e) {
            // 捕获令牌过期异常
            e.printStackTrace();
            map.put("msg", "token过期！");
        } catch (AlgorithmMismatchException e) {
            // 捕获令牌算法不一致异常
            e.printStackTrace();
            map.put("msg", "token算法不一致！");
        } catch (Exception e) {
            // 捕获其他异常
            e.printStackTrace();
            map.put("msg", "token无效！");
        }
        // 设置验证失败状态
        map.put("state", false);
        // 将验证结果转换为JSON字符串
        String json = new ObjectMapper().writeValueAsString(map);
        // 设置响应内容类型和字符集
        response.setContentType("application/json;charset=UTF-8");
        // 输出验证失败信息
        response.getWriter().println(json);

        // 验证失败，中断请求
        return false;
    }
}
