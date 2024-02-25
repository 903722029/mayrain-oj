package com.mayrain.oj.judge.codesandbox;

import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;

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
