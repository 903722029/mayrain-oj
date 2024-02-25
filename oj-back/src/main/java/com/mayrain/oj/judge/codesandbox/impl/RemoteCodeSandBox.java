package com.mayrain.oj.judge.codesandbox.impl;

import com.mayrain.oj.judge.codesandbox.CodeSandBox;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 自己实现的远程代码沙箱
 **/
public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
