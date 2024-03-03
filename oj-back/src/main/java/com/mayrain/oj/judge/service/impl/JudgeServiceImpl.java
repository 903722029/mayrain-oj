package com.mayrain.oj.judge.service.impl;

import cn.hutool.json.JSONUtil;
import com.mayrain.oj.common.ErrorCode;
import com.mayrain.oj.exception.BusinessException;
import com.mayrain.oj.judge.codesandbox.CodeSandBox;
import com.mayrain.oj.judge.codesandbox.CodeSandBoxFactory;
import com.mayrain.oj.judge.codesandbox.CodeSandBoxProxy;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.mayrain.oj.judge.service.JudgeService;
import com.mayrain.oj.judge.strategy.JudgeContext;
import com.mayrain.oj.judge.strategy.JudgeManager;
import com.mayrain.oj.model.dto.question.JudgeCase;
import com.mayrain.oj.judge.codesandbox.model.JudgeInfo;
import com.mayrain.oj.model.entity.Question;
import com.mayrain.oj.model.entity.QuestionSubmit;
import com.mayrain.oj.model.enums.QuestionSubmitStatusEnum;
import com.mayrain.oj.service.QuestionService;
import com.mayrain.oj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 判题服务
 **/
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeManager judgeManager;

    @Value("${codeSandBox.type:example}")
    private String type;
    @Override
    public JudgeInfo doJudge(long questionSubmitId) {
        // 1) 根据提交id，获取题目信息（包括编程语言、代码等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }
        // 2) 如果题目不为等待状态，就不用判题了
        if (!QuestionSubmitStatusEnum.WAITING.getValue().equals(questionSubmit.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "状态异常");
        }
        // 3) 更改判题状态为判题中，防止重复判题
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        // 4) 调用沙箱，获取执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInputCase).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.excuteCode(executeCodeRequest);
        // 5) 根据沙箱的结果，设置题目的判题信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setInputList(inputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6) 修改判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题结果更新失败");
        }
        return judgeInfo;
    }
}
