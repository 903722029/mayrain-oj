package com.mayrain.ojcodesandbox.danger;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-03
 * @Description:
 **/
public class Overtime {
    public static void main(String[] args) throws InterruptedException {
        long ONE_HOUR = 60 * 60 * 1000L;
        Thread.sleep(ONE_HOUR);
        System.out.println("无限睡眠");
    }
}
