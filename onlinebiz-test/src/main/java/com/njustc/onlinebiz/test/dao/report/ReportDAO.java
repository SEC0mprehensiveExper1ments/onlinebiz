package com.njustc.onlinebiz.test.dao.report;

import com.njustc.onlinebiz.common.model.test.report.Report;

public interface ReportDAO {
  Report insertReport(Report report);

  Report findReportById(String id);

  boolean updateContent(String id, Report.ReportContent content);

  boolean deleteReport(String id);
}
