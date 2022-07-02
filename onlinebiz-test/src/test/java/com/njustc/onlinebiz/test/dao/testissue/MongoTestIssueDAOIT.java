package com.njustc.onlinebiz.test.dao.testissue;

import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoTestIssueDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoTestIssueDAOIT {
  // 创建的测试问题ID集合
  private static final List<String> testIssueIds = new ArrayList<>();

  @Autowired private MongoTestIssueDAO mongoTestIssueDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertTestIssue() {
    // 创建100个测试问题
    for (int i = 0; i < 100; i++) {
      TestIssueList testIssueList = new TestIssueList();
      TestIssueList insertedTestIssueList = mongoTestIssueDAO.insertTestIssueList(testIssueList);
      Assertions.assertNotNull(insertedTestIssueList);
      Assertions.assertNotNull(insertedTestIssueList.getId());
      testIssueIds.add(insertedTestIssueList.getId());
    }
  }

  @Test
  @Order(2)
  public void findTestIssueListByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    TestIssueList testIssueList = mongoTestIssueDAO.findTestIssueListById(nonExistId);
    Assertions.assertNull(testIssueList);
  }

  @Test
  @Order(3)
  public void findTestIssueListById() {
    // 查找一个存在的ID
    for (String testIssueId : testIssueIds) {
      TestIssueList testIssueList = mongoTestIssueDAO.findTestIssueListById(testIssueId);
      if (testIssueList == null) {
        Assertions.fail("testIssueList is null");
      } else {
        Assertions.assertEquals(testIssueId, testIssueList.getId());
      }
      Assertions.assertNotNull(testIssueList);
      Assertions.assertEquals(testIssueId, testIssueList.getId());
    }
  }

  @Test
  @Order(4)
  public void updateContentByIdNonexistent() {
    // 查找一个不存在的ID
    Assertions.assertFalse(mongoTestIssueDAO.updateContent(getNonExistId(), new ArrayList<>()));
  }

  @Test
  @Order(5)
  public void updateContentById() {
    // 查找一个存在的ID
    for (String testIssueId : testIssueIds) {
      List<TestIssueList.TestIssue> testIssues = new ArrayList<>();
      TestIssueList.TestIssue testIssue = new TestIssueList.TestIssue();
      testIssue.setTestIssueId("testIssueId");
      testIssue.setDescription("description");
      testIssue.setCorrespondingRequirement("correspondingRequirement");
      testIssue.setInitialConditions("initialConditions");
      testIssue.setSpecificOperation("specificOperation");
      testIssue.setAssociatedCase("associatedCase");
      testIssue.setFindTime("findTime");
      testIssue.setResponsiblePerson("responsiblePerson");
      testIssue.setSuggestion("suggestion");
      testIssues.add(testIssue);
      Assertions.assertTrue(mongoTestIssueDAO.updateContent(testIssueId, testIssues));
    }
  }

  @Test
  @Order(6)
  public void deleteTestIssueByIdNonexistent() {
    // 查找一个不存在的ID
    Assertions.assertFalse(mongoTestIssueDAO.deleteTestIssueList(getNonExistId()));
  }

  @Test
  @Order(7)
  public void deleteTestIssueById() {
    // 查找一个存在的ID
    for (String testIssueId : testIssueIds) {
      Assertions.assertTrue(mongoTestIssueDAO.deleteTestIssueList(testIssueId));
    }
  }
}
