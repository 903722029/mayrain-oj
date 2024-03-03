package com.mayrain.oj.judge.codesandbox.impl;

import com.mayrain.oj.judge.codesandbox.CodeSandBox;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.mayrain.oj.judge.codesandbox.model.JudgeInfo;
import com.mayrain.oj.model.enums.JudgeInfoMessageEnum;
import com.mayrain.oj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 示例代码沙箱，只为了跑通流程
 **/
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        executeCodeResponse.setMessage("判题成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        return executeCodeResponse;
    }
}
