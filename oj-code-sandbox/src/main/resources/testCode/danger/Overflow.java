package com.mayrain.ojcodesandbox.danger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-03
 * @Description:
 **/
public class Main {
    public static void main(String[] args) {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            bytes.add(new byte[10000]);
        }
    }
}
