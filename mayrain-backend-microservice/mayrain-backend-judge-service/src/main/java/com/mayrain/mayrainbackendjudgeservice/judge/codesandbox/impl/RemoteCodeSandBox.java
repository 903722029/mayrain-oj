package com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mayrain.mayrainbackendcommon.common.ErrorCode;
import com.mayrain.mayrainbackendcommon.exception.BusinessException;
import com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeRequest;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 自己实现的远程代码沙箱
 **/
public class RemoteCodeSandBox implements CodeSandBox {
    public static final String AUTH_REQUEST_HEADER = "auth";
    public static final String AUTH_REQUEST_KEY = "secreteKey";
    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_KEY)
                .body(json).execute().body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandBox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
