package com.mayrain.oj.judge.service;

import com.mayrain.oj.judge.codesandbox.model.JudgeInfo;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
public interface JudgeService {
    /**
     * 代码沙箱判题
     * @param questionSubmitId 题目提交id
     * @return
     */
    JudgeInfo doJudge(long questionSubmitId);
}
