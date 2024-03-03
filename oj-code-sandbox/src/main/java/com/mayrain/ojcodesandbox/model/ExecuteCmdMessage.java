package com.mayrain.ojcodesandbox.model;

import lombok.Data;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-03
 * @Description:
 **/
@Data
public class ExecuteCmdMessage {
    private Integer exitValue;
    private String executeMessage;
    private String errorMessage;
    private Long time;
}
