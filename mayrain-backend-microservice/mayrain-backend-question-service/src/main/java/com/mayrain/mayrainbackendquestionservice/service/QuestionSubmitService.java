package com.mayrain.mayrainbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mayrain.mayrainbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import com.mayrain.mayrainbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.mayrain.mayrainbackendmodel.entity.QuestionSubmit;
import com.mayrain.mayrainbackendmodel.entity.User;
import com.mayrain.mayrainbackendmodel.vo.QuestionSubmitVO;

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

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
