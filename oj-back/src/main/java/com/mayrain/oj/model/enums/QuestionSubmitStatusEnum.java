package com.mayrain.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Si Yutong
 * @Date: 2024-01-19
 * @Description:
 **/
public enum QuestionSubmitStatusEnum {
    /**
     * 题目状态：0 - 待判题, 1 - 判题中, 2 - 成功, 3 - 失败
     */
    WAITING("待判题", 0),
    RUNNING("判题中", 1),
    SUCCESS("成功", 2),
    FAIL("失败", 3);
    private final String text;
    private final Integer value;

    QuestionSubmitStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(QuestionSubmitStatusEnum::getValue).collect(Collectors.toList());
    }

    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatusEnum questionSubmitStatusEnum : values()) {
            if (value.equals(questionSubmitStatusEnum.getValue())) {
                return questionSubmitStatusEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }
}
