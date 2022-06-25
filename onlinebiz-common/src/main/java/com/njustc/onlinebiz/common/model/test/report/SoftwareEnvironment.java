package com.njustc.onlinebiz.common.model.test.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SoftwareEnvironment {
  // 软件类别
  private String softwareType;
  // 软件名称
  private String softwareName;
  // 软件版本
  private String softwareVersion;
}
