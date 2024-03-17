package com.mayrain.ojcodesandbox.controller;

import com.mayrain.ojcodesandbox.JavaNativeCodeSandBox;
import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-02
 * @Description:
 **/
@RestController("/")
public class MainController {
    public static final String AUTH_REQUEST_HEADER = "auth";
    public static final String AUTH_REQUEST_KEY = "secreteKey";
    @Resource
    private JavaNativeCodeSandBox javaNativeCodeSandBox;
    @GetMapping("/health")
    public String  health() {
        return "ok";
    }

    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
            HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_KEY.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandBox.excuteCode(executeCodeRequest);
    }
}
