package com.mayrain.mayrainbackendjudgeservice.judge.strategy;


import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import com.mayrain.mayrainbackendmodel.entity.Question;
import com.mayrain.mayrainbackendmodel.entity.QuestionSubmit;
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
