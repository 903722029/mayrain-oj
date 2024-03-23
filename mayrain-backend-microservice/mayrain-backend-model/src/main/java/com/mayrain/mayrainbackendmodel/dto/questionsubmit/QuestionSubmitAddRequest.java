package com.mayrain.mayrainbackendmodel.dto.questionsubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Si Yutong
 * @Date: 2024-01-19
 * @Description:
 **/
@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
