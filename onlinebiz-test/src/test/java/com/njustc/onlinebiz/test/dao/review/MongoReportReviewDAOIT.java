package com.njustc.onlinebiz.test.dao.review;

import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoReportReviewDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoReportReviewDAOIT {
  // 创建的测试报告评审ID集合
  private static final List<String> reportReviewIds = new ArrayList<>();

  @Autowired private MongoReportReviewDAO mongoReportReviewDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertReportReview() {
    // 创建100个测试报告评审
    for (int i = 0; i < 100; i++) {
      ReportReview reportReview = new ReportReview();
      ReportReview insertedReportReview = mongoReportReviewDAO.insertReportReview(reportReview);
      Assertions.assertNotNull(insertedReportReview);
      Assertions.assertNotNull(insertedReportReview.getId());
      reportReviewIds.add(insertedReportReview.getId());
    }
  }

  @Test
  @Order(2)
  public void findReportReviewByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    ReportReview reportReview = mongoReportReviewDAO.findReportReviewById(nonExistId);
    Assertions.assertNull(reportReview);
  }

  @Test
  @Order(3)
  public void findReportReviewById() {
    // 查找一个存在的ID
    for (String reportReviewId : reportReviewIds) {
      ReportReview reportReview = mongoReportReviewDAO.findReportReviewById(reportReviewId);
      Assertions.assertNotNull(reportReview);
      Assertions.assertEquals(reportReviewId, reportReview.getId());
    }
  }

  @Test
  @Order(4)
  public void updateReportReviewByIdNonexistent() {
    // 更新一个不存在的ID
    Assertions.assertFalse(
        mongoReportReviewDAO.updateReportReview(getNonExistId(), new ReportReview()));
  }

  @Test
  @Order(5)
  public void updateReportReviewById() {
    // 更新一个存在的ID
    for (String reportReviewId : reportReviewIds) {
      ReportReview reportReview = new ReportReview();
      reportReview.setId(reportReviewId);
      Assertions.assertTrue(mongoReportReviewDAO.updateReportReview(reportReviewId, reportReview));
    }
  }

  @Test
  @Order(6)
  public void updateScannedCopyPathByIdNonexistent() {
    // 更新一个不存在的ID
    Assertions.assertFalse(mongoReportReviewDAO.updateScannedCopyPath(getNonExistId(), ""));
  }

  @Test
  @Order(7)
  public void updateScannedCopyPathById() {
    // 更新一个存在的ID
    for (String reportReviewId : reportReviewIds) {
      Assertions.assertTrue(mongoReportReviewDAO.updateScannedCopyPath(reportReviewId, ""));
    }
  }

  @Test
  @Order(8)
  public void deleteReportAuditByIdNonexistent() {
    // 删除一个不存在的ID
    Assertions.assertFalse(mongoReportReviewDAO.deleteReportAuditById(getNonExistId()));
  }

  @Test
  @Order(9)
  public void deleteReportAuditById() {
    // 删除一个存在的ID
    for (String reportReviewId : reportReviewIds) {
      Assertions.assertTrue(mongoReportReviewDAO.deleteReportAuditById(reportReviewId));
    }
  }
}
