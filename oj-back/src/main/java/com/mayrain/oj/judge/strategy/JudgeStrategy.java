package com.mayrain.oj.judge.strategy;

import com.mayrain.oj.judge.codesandbox.model.JudgeInfo;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 判题策略
 **/
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
