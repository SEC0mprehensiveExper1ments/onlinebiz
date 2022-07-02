package com.njustc.onlinebiz.test.dao.report;

import com.njustc.onlinebiz.common.model.test.report.Report;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoReportDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoReportDAOIT {
  // 创建的测试报告ID集合
  private static final List<String> reportIds = new ArrayList<>();
  @Autowired private MongoReportDAO mongoReportDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertReport() {
    // 创建100个测试报告
    for (int i = 0; i < 100; i++) {
      Report report = new Report();
      report.setContent(new Report.ReportContent());
      report.setEntrustId(getNonExistId());
      report.setProjectId(getNonExistId());
      Report insertedReport = mongoReportDAO.insertReport(report);
      Assertions.assertNotNull(insertedReport);
      Assertions.assertNotNull(insertedReport.getId());
      reportIds.add(insertedReport.getId());
    }
  }

  @Test
  @Order(2)
  public void findReportByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Report report = mongoReportDAO.findReportById(nonExistId);
    Assertions.assertNull(report);
  }

  @Test
  @Order(3)
  public void findReportById() {
    // 查找一个存在的ID
    String existId = reportIds.get(0);
    Report report = mongoReportDAO.findReportById(existId);
    Assertions.assertNotNull(report);
    Assertions.assertEquals(existId, report.getId());
  }

  @Test
  @Order(4)
  public void updateContentByIdNonexistent() {
    // 更新一个不存在的ID
    String nonExistId = getNonExistId();
    Report.ReportContent content = new Report.ReportContent();
    boolean result = mongoReportDAO.updateContent(nonExistId, content);
    Assertions.assertFalse(result);
  }

  @Test
  @Order(5)
  public void updateContentById() {
    for (String id : reportIds) {
      Report.ReportContent content = new Report.ReportContent();
      boolean result = mongoReportDAO.updateContent(id, content);
      Assertions.assertTrue(result);
    }
  }

  @Test
  @Order(6)
  public void deleteReportByIdNonexistent() {
    // 删除一个不存在的ID
    String nonExistId = getNonExistId();
    boolean result = mongoReportDAO.deleteReport(nonExistId);
    Assertions.assertFalse(result);
  }

  @Test
  @Order(7)
  public void deleteReportById() {
    for (String id : reportIds) {
      boolean result = mongoReportDAO.deleteReport(id);
      Assertions.assertTrue(result);
    }
  }
}
