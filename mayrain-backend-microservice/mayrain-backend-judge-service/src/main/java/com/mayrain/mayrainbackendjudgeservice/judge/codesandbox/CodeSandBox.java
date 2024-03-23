package com.mayrain.mayrainbackendjudgeservice.judge.codesandbox;


import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeRequest;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 代码沙箱
 **/

public interface CodeSandBox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest);
}
