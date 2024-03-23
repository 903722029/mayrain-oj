package com.mayrain.mayrainbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mayrain.mayrainbackendmodel.dto.question.QuestionQueryRequest;
import com.mayrain.mayrainbackendmodel.entity.Question;
import com.mayrain.mayrainbackendmodel.entity.QuestionSubmit;
import com.mayrain.mayrainbackendmodel.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

/**
* @author 90372
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-01-16 18:05:18
*/
@FeignClient(name = "mayrain-backend-question-service", path = "api/question/inner")
public interface QuestionFeignClient {
  @GetMapping("/get/id")
  Question getQuestionById(@RequestParam("questionId") Long questionId);

  @GetMapping("/question_submit/get/id")
  QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

  @PostMapping("/question_submit/update")
  boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
