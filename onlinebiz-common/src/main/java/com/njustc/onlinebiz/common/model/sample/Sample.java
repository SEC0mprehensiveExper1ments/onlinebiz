package com.njustc.onlinebiz.common.model.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Sample {
  // 存放地点
  private String storageLocation;
  // 样品编号
  private String sampleCode;
  // 样品名称
  private String sampleName;
  // 样品类型
  private String sampleType;
  // 样品描述
  private String sampleDesc;
}
