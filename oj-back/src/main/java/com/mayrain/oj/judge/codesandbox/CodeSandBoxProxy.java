package com.mayrain.oj.judge.codesandbox;

import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/

@AllArgsConstructor
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox{
    private final CodeSandBox codeSandBox;

    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("进入代码沙箱");
        ExecuteCodeResponse executeCodeResponse = codeSandBox.excuteCode(executeCodeRequest);
        log.info("结束代码沙箱:" + executeCodeResponse);
        return executeCodeResponse;
    }

}
