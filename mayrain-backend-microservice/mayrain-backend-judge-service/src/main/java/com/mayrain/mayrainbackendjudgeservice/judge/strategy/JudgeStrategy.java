package com.mayrain.mayrainbackendjudgeservice.judge.strategy;


import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description: 判题策略
 **/
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
