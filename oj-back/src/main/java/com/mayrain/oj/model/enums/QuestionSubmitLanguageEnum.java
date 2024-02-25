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
public enum QuestionSubmitLanguageEnum {
    JAVA("java", "java"),
    CPP("cpp", "cpp"),
    GOLANG("go", "go"),
    PYTHON("python", "python");
    private final String text;
    private final String value;

    QuestionSubmitLanguageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(QuestionSubmitLanguageEnum::getValue).collect(Collectors.toList());
    }

    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitLanguageEnum questionSubmitStatusEnum : values()) {
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
