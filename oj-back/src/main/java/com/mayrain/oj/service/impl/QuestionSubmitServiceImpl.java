package com.mayrain.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayrain.oj.common.ErrorCode;
import com.mayrain.oj.exception.BusinessException;
import com.mayrain.oj.mapper.QuestionSubmitMapper;
import com.mayrain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.mayrain.oj.model.entity.Question;
import com.mayrain.oj.model.entity.QuestionSubmit;
import com.mayrain.oj.model.entity.User;
import com.mayrain.oj.service.PostThumbService;
import com.mayrain.oj.service.QuestionService;
import com.mayrain.oj.service.QuestionSubmitService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 90372
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-01-16 18:18:38
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;

    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setStatus(0);
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return questionSubmit.getId();
    }
}




