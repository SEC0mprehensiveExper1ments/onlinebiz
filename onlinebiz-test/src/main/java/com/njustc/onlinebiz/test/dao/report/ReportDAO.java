package com.njustc.onlinebiz.test.dao.report;

import com.njustc.onlinebiz.common.model.test.report.Report;

/**
 * 报告dao层接口
 *
 */
public interface ReportDAO {
  /**
   * 插入报告
   *
   * @param report 报告
   * @return {@link Report}
   */
  Report insertReport(Report report);

  /**
   * 发现报告id
   *
   * @param id id
   * @return {@link Report}
   */
  Report findReportById(String id);

  /**
   * 更新内容
   *
   * @param id id
   * @param content 内容
   * @return boolean
   */
  boolean updateContent(String id, Report.ReportContent content);

  /**
   * 删除报告
   *
   * @param id id
   * @return boolean
   */
  boolean deleteReport(String id);
}
