package com.njustc.onlinebiz.test.dao.testrecord;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoTestRecordDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoTestRecordDAOIT {
  // 创建的测试记录ID集合
  private static final List<String> testRecordIds = new ArrayList<>();

  @Autowired private MongoTestRecordDAO mongoTestRecordDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertTestRecord() {
    // 创建100个测试记录
    for (int i = 0; i < 100; i++) {
      TestRecordList testRecordList = new TestRecordList();
      TestRecordList insertedTestRecordList =
          mongoTestRecordDAO.insertTestRecordList(testRecordList);
      Assertions.assertNotNull(insertedTestRecordList);
      Assertions.assertNotNull(insertedTestRecordList.getId());
      testRecordIds.add(insertedTestRecordList.getId());
    }
  }

  @Test
  @Order(2)
  public void findTestRecordListByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    TestRecordList testRecordList = mongoTestRecordDAO.findTestRecordListById(nonExistId);
    Assertions.assertNull(testRecordList);
  }

  @Test
  @Order(3)
  public void findTestRecordListById() {
    // 查找一个存在的ID
    for (String testRecordId : testRecordIds) {
      TestRecordList testRecordList = mongoTestRecordDAO.findTestRecordListById(testRecordId);
      if (testRecordList == null) {
        Assertions.fail("testRecordList is null");
      } else {
        Assertions.assertEquals(testRecordId, testRecordList.getId());
      }
    }
  }

  @Test
  @Order(4)
  public void updateContentByIdNonexistent() {
    // 查找一个不存在的ID
    Assertions.assertFalse(mongoTestRecordDAO.updateContent(getNonExistId(), new ArrayList<>()));
  }

  @Test
  @Order(5)
  public void updateContentById() {
    // 查找一个存在的ID
    for (String testRecordId : testRecordIds) {
      Assertions.assertTrue(mongoTestRecordDAO.updateContent(testRecordId, new ArrayList<>()));
    }
  }

  @Test
  @Order(6)
  public void deleteTestRecordListByIdNonexistent() {
    // 查找一个不存在的ID
    Assertions.assertFalse(mongoTestRecordDAO.deleteTestRecordList(getNonExistId()));
  }

  @Test
  @Order(7)
  public void deleteTestRecordListById() {
    // 查找一个存在的ID
    for (String testRecordId : testRecordIds) {
      Assertions.assertTrue(mongoTestRecordDAO.deleteTestRecordList(testRecordId));
    }
  }
}
