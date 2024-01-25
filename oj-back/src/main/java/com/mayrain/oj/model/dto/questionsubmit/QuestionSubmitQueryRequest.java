package com.mayrain.oj.model.dto.questionsubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mayrain.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Si Yutong
 * @Date: 2024-01-19
 * @Description:
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 语言
     */
    private String language;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户 id
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
