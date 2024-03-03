package com.mayrain.oj.judge.strategy;

import com.mayrain.oj.judge.codesandbox.model.JudgeInfo;
import com.mayrain.oj.model.entity.Question;
import com.mayrain.oj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 策略的参数
 **/
@Data
public class JudgeContext {
    private List<String> outputList;
    private List<String> inputList;
    private Question question;
    private QuestionSubmit questionSubmit;
    private JudgeInfo judgeInfo;
}
