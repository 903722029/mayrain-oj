package com.mayrain.oj.model.dto.questionsubmit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Si Yutong
 * @Date: 2024-01-17
 * @Description:
 **/
@NoArgsConstructor
@Data
public class JudgeInfo {


  private String message;

  private Long time;

  private Long memory;
}
