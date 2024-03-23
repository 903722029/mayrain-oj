package com.mayrain.mayrainbackendjudgeservice.controller.inner;

import com.mayrain.mayrainbackendjudgeservice.judge.service.JudgeService;
import com.mayrain.mayrainbackendmodel.codesandbox.JudgeInfo;
import com.mayrain.mayrainbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-23
 * @Description:
 **/
@RestController()
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;
    /**
     * 代码沙箱判题
     * @param questionSubmitId 题目提交id
     * @return
     */
    @PostMapping("/do")
    @Override
    public JudgeInfo doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
