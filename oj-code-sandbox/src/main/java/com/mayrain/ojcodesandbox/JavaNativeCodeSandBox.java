package com.mayrain.ojcodesandbox;

import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-16
 * @Description:
 **/
@Component
public class JavaNativeCodeSandBox extends JavaCodeSandBoxTemplate{
    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        return super.excuteCode(executeCodeRequest);
    }
}
