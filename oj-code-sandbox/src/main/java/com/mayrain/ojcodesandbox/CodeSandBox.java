package com.mayrain.ojcodesandbox;

import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;

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
