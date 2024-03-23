package com.mayrain.mayrainbackendjudgeservice.judge.service.impl;

import cn.hutool.json.JSONUtil;
import com.mayrain.mayrainbackendcommon.common.ErrorCode;
import com.mayrain.mayrainbackendcommon.exception.BusinessException;
import com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.mayrain.mayrainbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.mayrain.mayrainbackendjudgeservice.judge.service.JudgeService;
import com.mayrain.mayrainbackendjudgeservice.judge.strategy.JudgeContext;
import com.mayrain.mayrainbackendjudgeservice.judge.strategy.JudgeManager;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeRequest;
import com.mayrain.mayrainbackendmodel.codesandbox.ExecuteCodeResponse;
import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import com.mayrain.mayrainbackendmodel.dto.question.JudgeCase;
import com.mayrain.mayrainbackendmodel.entity.Question;
import com.mayrain.mayrainbackendmodel.entity.QuestionSubmit;
import com.mayrain.mayrainbackendmodel.enums.QuestionSubmitStatusEnum;
import com.mayrain.mayrainbackendserviceclient.service.QuestionFeignClient;
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
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;
    @Value("${codeSandBox.type:example}")
    private String type;
    @Override
    public JudgeInfo doJudge(long questionSubmitId) {
        // 1) 根据提交id，获取题目信息（包括编程语言、代码等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
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
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
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
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题结果更新失败");
        }
        return judgeInfo;
    }
}
