package com.mayrain.ojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-02
 * @Description:
 **/
@RestController("/")
public class MainController {
    @GetMapping("/health")
    public String  health() {
        return "ok";
    }
}
