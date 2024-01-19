package com.mayrain.oj.model.dto.question;

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
public class JudgeConfig {

  private Long timeLimit;

  private Long memoryLimit;

  private Long stackLimit;
}
