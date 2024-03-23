package com.mayrain.mayrainbackendserviceclient.service;


import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
@FeignClient(name = "mayrain-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 代码沙箱判题
     * @param questionSubmitId 题目提交id
     * @return
     */
    @PostMapping("/do")
    JudgeInfo doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
