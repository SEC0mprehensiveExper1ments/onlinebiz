package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoSchemeDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoSchemeDAOIT {
  // 创建的测试方案ID集合
  private static final List<String> schemeIds = new ArrayList<>();

  @Autowired private MongoSchemeDAO mongoSchemeDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertScheme() {
    // 创建100个测试方案
    for (int i = 0; i < 100; i++) {
      Scheme scheme = new Scheme();
      Scheme insertedScheme = mongoSchemeDAO.insertScheme(scheme);
      Assertions.assertNotNull(insertedScheme);
      Assertions.assertNotNull(insertedScheme.getId());
      schemeIds.add(insertedScheme.getId());
    }
  }

  @Test
  @Order(2)
  public void findSchemeByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Scheme scheme = mongoSchemeDAO.findSchemeById(nonExistId);
    Assertions.assertNull(scheme);
  }

  @Test
  @Order(3)
  public void findSchemeById() {
    // 查找一个存在的ID
    for (String schemeId : schemeIds) {
      Scheme scheme = mongoSchemeDAO.findSchemeById(schemeId);
      Assertions.assertNotNull(scheme);
      Assertions.assertEquals(schemeId, scheme.getId());
    }
  }

  @Test
  @Order(4)
  public void updateSchemeByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Scheme scheme = mongoSchemeDAO.findSchemeById(nonExistId);
    Assertions.assertNull(scheme);
  }

  @Test
  @Order(5)
  public void updateSchemeById() {
    // 查找一个存在的ID
    for (String schemeId : schemeIds) {
      Scheme scheme = mongoSchemeDAO.findSchemeById(schemeId);
      Assertions.assertNotNull(scheme);
      Assertions.assertEquals(schemeId, scheme.getId());
    }
  }

  @Test
  @Order(6)
  public void deleteSchemeByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    boolean result = mongoSchemeDAO.deleteScheme(nonExistId);
    Assertions.assertFalse(result);
  }

  @Test
  @Order(7)
  public void deleteSchemeById() {
    // 查找一个存在的ID
    for (String schemeId : schemeIds) {
      boolean result = mongoSchemeDAO.deleteScheme(schemeId);
      Assertions.assertTrue(result);
    }
  }
}
