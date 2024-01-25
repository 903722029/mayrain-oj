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
public enum JudgeInfoMessageEnum {
    /**
     * 题目状态：0 - 待判题, 1 - 判题中, 2 - 成功, 3 - 失败
     */
    ACCEPTED("Accepted", "成功"),
    WRONG_ANSWER("Wrong Answer", "答案错误"),
    COMPILE_ERROR("Compile Error", "编译错误"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "超出内存限制"),
    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"),
    RUNTIME_EXCEPTION("Runtime Exception", "运行时错误"),
    SYSTEM_ERROR("System Error", "系统错误");

    private final String text;
    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(JudgeInfoMessageEnum::getValue).collect(Collectors.toList());
    }

    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum questionSubmitStatusEnum : values()) {
            if (value.equals(questionSubmitStatusEnum.getValue())) {
                return questionSubmitStatusEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
