package com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.impl;


import com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeRequest;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 第三方代码沙箱
 **/
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
