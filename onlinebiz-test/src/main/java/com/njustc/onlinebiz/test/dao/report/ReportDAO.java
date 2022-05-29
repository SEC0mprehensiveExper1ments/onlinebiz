package com.njustc.onlinebiz.test.dao.report;

import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.common.model.test.report.ReportStatus;

public interface ReportDAO {
  Report insertReport(Report report);

  Report findReportById(String id);

  boolean updateContent(String id, Report.ReportContent content);

  boolean updateStatus(String id, ReportStatus status);

  boolean deleteReport(String id);
}
