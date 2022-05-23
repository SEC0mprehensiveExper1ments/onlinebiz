package com.njustc.onlinebiz.test.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatus {
  private ReportStage stage;
  private String status;
}
