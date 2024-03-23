package com.mayrain.mayrainbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import com.mayrain.mayrainbackendmodel.dto.question.JudgeCase;
import com.mayrain.mayrainbackendmodel.dto.question.JudgeConfig;
import com.mayrain.mayrainbackendmodel.entity.Question;
import com.mayrain.mayrainbackendmodel.enums.JudgeInfoMessageEnum;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 默认判题策略
 **/
public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        JudgeInfoMessageEnum judgeInfoMessage = JudgeInfoMessageEnum.WRONG_ANSWER;
        // 如果输出用例大小不同，直接判断为错误
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> expectedOutputList = judgeCaseList.stream().map(JudgeCase::getOutputCase).collect(Collectors.toList());
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        if (outputList.size() != expectedOutputList.size()) {
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        // 依次比较判题结果
        for (int i = 0; i < judgeCaseList.size(); i++) {
            String expectedOutputCase = judgeCaseList.get(i).getOutputCase();
            String outputCase = outputList.get(i);
            if (!expectedOutputCase.equals(outputCase)) {
                judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
                return judgeInfoResponse;
            }
        }
        // 判题限制判断
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long expectedTimeLimit = judgeConfig.getTimeLimit();
        Long expectedMemoryLimit = judgeConfig.getMemoryLimit();
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();
        if (time > expectedTimeLimit) {
            judgeInfoMessage = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        if (memory > expectedMemoryLimit) {
            judgeInfoMessage = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        judgeInfoMessage = JudgeInfoMessageEnum.ACCEPTED;
        // 返回判题信息
        judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);
        return judgeInfoResponse;
    }
}
