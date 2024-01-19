package com.mayrain.oj.service;

import com.mayrain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.mayrain.oj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mayrain.oj.model.entity.User;

/**
* @author 90372
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-01-16 18:18:38
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
