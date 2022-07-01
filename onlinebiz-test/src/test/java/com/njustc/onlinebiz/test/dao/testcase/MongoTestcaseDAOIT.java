package com.njustc.onlinebiz.test.dao.testcase;

import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoTestcaseDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoTestcaseDAOIT {
  // 创建的测试用例ID集合
  private static final List<String> testcaseIds = new ArrayList<>();

  @Autowired private MongoTestcaseDAO mongoTestcaseDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertTestcase() {
    // 创建100个测试用例
    for (int i = 0; i < 100; i++) {
      Testcase testcase = new Testcase();
      Testcase insertedTestcase = mongoTestcaseDAO.insertTestcaseList(testcase);
      Assertions.assertNotNull(insertedTestcase);
      Assertions.assertNotNull(insertedTestcase.getId());
      testcaseIds.add(insertedTestcase.getId());
    }
  }

  @Test
  @Order(2)
  public void findTestcaseByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Testcase testcase = mongoTestcaseDAO.findTestcaseListById(nonExistId);
    Assertions.assertNull(testcase);
  }

  @Test
  @Order(3)
  public void findTestcaseById() {
    // 查找一个存在的ID
    for (String testcaseId : testcaseIds) {
      Testcase testcase = mongoTestcaseDAO.findTestcaseListById(testcaseId);
      Assertions.assertNotNull(testcase);
      Assertions.assertEquals(testcaseId, testcase.getId());
    }
  }

  @Test
  @Order(4)
  public void updateTestcaseByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Testcase testcase = mongoTestcaseDAO.findTestcaseListById(nonExistId);
    Assertions.assertNull(testcase);
  }

  @Test
  @Order(5)
  public void updateTestcaseById() {
    // 查找一个存在的ID
    for (String testcaseId : testcaseIds) {
      Testcase testcase = mongoTestcaseDAO.findTestcaseListById(testcaseId);
      Assertions.assertNotNull(testcase);
      Assertions.assertEquals(testcaseId, testcase.getId());
    }
  }

  @Test
  @Order(6)
  public void deleteTestcaseByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Testcase testcase = mongoTestcaseDAO.findTestcaseListById(nonExistId);
    Assertions.assertNull(testcase);
  }

  @Test
  @Order(7)
  public void deleteTestcaseById() {
    // 查找一个存在的ID
    for (String testcaseId : testcaseIds) {
      Testcase testcase = mongoTestcaseDAO.findTestcaseListById(testcaseId);
      Assertions.assertNotNull(testcase);
      Assertions.assertEquals(testcaseId, testcase.getId());
    }
  }
}
