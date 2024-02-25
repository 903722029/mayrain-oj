package com.mayrain.oj.judge.codesandbox.model;

import com.mayrain.oj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 输出结果
     */
    private List<String> outputList;
    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
    /**
     * 执行信息
     */
    private String message;
    /**
     * 执行状态
     */
    private Integer status;

}
