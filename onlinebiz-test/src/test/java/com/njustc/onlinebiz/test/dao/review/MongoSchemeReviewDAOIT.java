package com.njustc.onlinebiz.test.dao.review;

import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoSchemeReviewDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoSchemeReviewDAOIT {
  // 创建的测试方案评审ID集合
  private static final List<String> schemeReviewIds = new ArrayList<>();

  @Autowired private MongoSchemeReviewDAO mongoSchemeReviewDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertSchemeReview() {
    // 创建100个测试方案评审
    for (int i = 0; i < 100; i++) {
      SchemeReview schemeReview = new SchemeReview();
      SchemeReview insertedSchemeReview = mongoSchemeReviewDAO.insertSchemeReview(schemeReview);
      Assertions.assertNotNull(insertedSchemeReview);
      Assertions.assertNotNull(insertedSchemeReview.getId());
      schemeReviewIds.add(insertedSchemeReview.getId());
    }
  }

  @Test
  @Order(2)
  public void findSchemeReviewByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    SchemeReview schemeReview = mongoSchemeReviewDAO.findSchemeReviewById(nonExistId);
    Assertions.assertNull(schemeReview);
  }

  @Test
  @Order(3)
  public void findSchemeReviewById() {
    // 查找一个存在的ID
    for (String schemeReviewId : schemeReviewIds) {
      SchemeReview schemeReview = mongoSchemeReviewDAO.findSchemeReviewById(schemeReviewId);
      Assertions.assertNotNull(schemeReview);
      Assertions.assertEquals(schemeReviewId, schemeReview.getId());
    }
  }

  @Test
  @Order(4)
  public void updateSchemeReviewByIdNonexistent() {
    // 更新一个不存在的ID
    Assertions.assertFalse(
        mongoSchemeReviewDAO.updateSchemeReview(getNonExistId(), new SchemeReview()));
  }

  @Test
  @Order(5)
  public void updateSchemeReviewById() {
    // 更新一个存在的ID
    for (String schemeReviewId : schemeReviewIds) {
      SchemeReview schemeReview = new SchemeReview();
      schemeReview.setId(schemeReviewId);
      Assertions.assertTrue(mongoSchemeReviewDAO.updateSchemeReview(schemeReviewId, schemeReview));
    }
  }

  @Test
  @Order(6)
  public void updateScannedCopyPathByIdNonexistent() {
    // 更新一个不存在的ID
    Assertions.assertFalse(mongoSchemeReviewDAO.updateScannedCopyPath(getNonExistId(), ""));
  }

  @Test
  @Order(7)
  public void updateScannedCopyPathById() {
    // 更新一个存在的ID
    for (String schemeReviewId : schemeReviewIds) {
      Assertions.assertTrue(mongoSchemeReviewDAO.updateScannedCopyPath(schemeReviewId, ""));
    }
  }

  @Test
  @Order(8)
  public void deleteSchemeAuditByIdNonexistent() {
    // 删除一个不存在的ID
    Assertions.assertFalse(mongoSchemeReviewDAO.deleteSchemeAuditById(getNonExistId()));
  }

  @Test
  @Order(9)
  public void deleteSchemeAuditById() {
    // 删除一个存在的ID
    for (String schemeReviewId : schemeReviewIds) {
      Assertions.assertTrue(mongoSchemeReviewDAO.deleteSchemeAuditById(schemeReviewId));
    }
  }
}
