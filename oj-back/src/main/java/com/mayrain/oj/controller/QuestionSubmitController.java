package com.mayrain.oj.controller;

import com.mayrain.oj.common.BaseResponse;
import com.mayrain.oj.common.ErrorCode;
import com.mayrain.oj.common.ResultUtils;
import com.mayrain.oj.exception.BusinessException;
import com.mayrain.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.mayrain.oj.model.entity.User;
import com.mayrain.oj.service.QuestionSubmitService;
import com.mayrain.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Si Yutong
 * @Date: 2024-01-16
 * @Description:
 **/
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 题目提交 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                         HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0 || StringUtils.isEmpty(
                questionSubmitAddRequest.getLanguage()) || StringUtils.isEmpty(questionSubmitAddRequest.getCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

}