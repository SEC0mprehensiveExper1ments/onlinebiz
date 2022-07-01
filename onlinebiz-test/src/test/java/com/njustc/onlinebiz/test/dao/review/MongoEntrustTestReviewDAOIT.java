package com.njustc.onlinebiz.test.dao.review;

import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoEntrustTestReviewDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoEntrustTestReviewDAOIT {
  // 创建的测试报告ID集合
  private static final List<String> entrustTestReviewIds = new ArrayList<>();
  @Autowired private MongoEntrustTestReviewDAO mongoEntrustTestReviewDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertEntrustTestReview() {
    // 创建100个测试报告
    for (int i = 0; i < 100; i++) {
      EntrustTestReview entrustTestReview = new EntrustTestReview();
      entrustTestReview.setProjectId(getNonExistId());
      EntrustTestReview insertedEntrustTestReview =
          mongoEntrustTestReviewDAO.insertEntrustTestReview(entrustTestReview);
      Assertions.assertNotNull(insertedEntrustTestReview);
      Assertions.assertNotNull(insertedEntrustTestReview.getId());
      entrustTestReviewIds.add(insertedEntrustTestReview.getId());
    }
  }

  @Test
  @Order(2)
  public void findEntrustTestReviewByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    EntrustTestReview entrustTestReview =
        mongoEntrustTestReviewDAO.findEntrustTestReviewById(nonExistId);
    Assertions.assertNull(entrustTestReview);
  }

  @Test
  @Order(3)
  public void findEntrustTestReviewById() {
    for (String id : entrustTestReviewIds) {
      EntrustTestReview entrustTestReview = mongoEntrustTestReviewDAO.findEntrustTestReviewById(id);
      Assertions.assertNotNull(entrustTestReview);
      Assertions.assertEquals(id, entrustTestReview.getId());
    }
  }

  @Test
  @Order(4)
  public void updateEntrustTestReviewByIdNonexistent() {
    // 更新一个不存在的ID
    Assertions.assertFalse(
        mongoEntrustTestReviewDAO.updateEntrustTestReview(
            getNonExistId(), new EntrustTestReview()));
  }

  @Test
  @Order(5)
  public void updateEntrustTestReviewById() {
    // 更新一个存在的ID
    for (String id : entrustTestReviewIds) {
      EntrustTestReview entrustTestReview = new EntrustTestReview();
      entrustTestReview.setId(id);
      entrustTestReview.setProjectId(getNonExistId());
      Assertions.assertTrue(
          mongoEntrustTestReviewDAO.updateEntrustTestReview(id, entrustTestReview));
    }
  }

  @Test
  @Order(6)
  public void deleteEntrustTestReviewByIdNonexistent() {
    // 删除一个不存在的ID
    Assertions.assertFalse(mongoEntrustTestReviewDAO.deleteEntrustTestReviewById(getNonExistId()));
  }

  @Test
  @Order(7)
  public void deleteEntrustTestReviewById() {
    // 删除一个存在的ID
    for (String id : entrustTestReviewIds) {
      Assertions.assertTrue(mongoEntrustTestReviewDAO.deleteEntrustTestReviewById(id));
    }
  }
}
