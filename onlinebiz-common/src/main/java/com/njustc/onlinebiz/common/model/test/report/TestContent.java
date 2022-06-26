package com.njustc.onlinebiz.common.model.test.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TestContent {
  // 测试内容
  private String content;
  // 描述
  private String description;
  // 测试结果
  private String result;
}
