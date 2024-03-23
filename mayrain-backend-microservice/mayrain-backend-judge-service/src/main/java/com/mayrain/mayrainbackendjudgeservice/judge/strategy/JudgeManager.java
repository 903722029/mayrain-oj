package com.mayrain.mayrainbackendjudgeservice.judge.strategy;

import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import com.mayrain.mayrainbackendmodel.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
