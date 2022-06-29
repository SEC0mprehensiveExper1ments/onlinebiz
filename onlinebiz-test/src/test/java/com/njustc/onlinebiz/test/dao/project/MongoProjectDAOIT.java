package com.njustc.onlinebiz.test.dao.project;

import com.njustc.onlinebiz.common.model.test.project.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Import(MongoProjectDAO.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class MongoProjectDAOIT {
  // 创建的测试项目ID集合
  private static final List<String> projectIds = new ArrayList<>();
  @Autowired private MongoProjectDAO mongoProjectDAO;

  // 生成一个不存在的ID
  private String getNonExistId() {
    return new ObjectId(new Date(0)).toString();
  }

  @Test
  @Order(1)
  public void insertProject() {
    // 创建100个测试项目
    for (int i = 0; i < 100; i++) {
      Project project = new Project();
      ProjectBaseInfo projectBaseInfo = new ProjectBaseInfo();
      projectBaseInfo.setMarketerId(i / 10L);
      projectBaseInfo.setTesterId(i / 10L);
      projectBaseInfo.setQaId(i / 10L);
      projectBaseInfo.setEntrustId(getNonExistId());
      projectBaseInfo.setSoftwareName("softwareName" + i);
      projectBaseInfo.setCustomerId(i / 10L);
      projectBaseInfo.setSerialNumber("serialNumber" + i);
      project.setProjectBaseInfo(projectBaseInfo);
      project.setProjectFormIds(new ProjectFormIds());
      project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
      Project insertedProject = mongoProjectDAO.insertProject(project);
      Assertions.assertNotNull(insertedProject);
      Assertions.assertNotNull(insertedProject.getId());
      projectIds.add(insertedProject.getId());
    }
  }

  @Test
  @Order(2)
  public void findProjectByIdNonexistent() {
    // 查找一个不存在的ID
    String nonExistId = getNonExistId();
    Project project = mongoProjectDAO.findProjectById(nonExistId);
    Assertions.assertNull(project);
  }

  @Test
  @Order(3)
  public void findProjectByIdExistent() {
    for (String projectId : projectIds) {
      Project project = mongoProjectDAO.findProjectById(projectId);
      Assertions.assertNotNull(project);
      Assertions.assertEquals(projectId, project.getId());
    }
  }

  @Test
  @Order(4)
  public void findAllProjects() {
    List<ProjectOutline> projects = mongoProjectDAO.findAllProjects(1, 10);
    Assertions.assertNotNull(projects);
    Assertions.assertEquals(10, projects.size());
  }

  @Test
  @Order(5)
  public void countAll() {
    long count = mongoProjectDAO.countAll();
    Assertions.assertTrue(count >= 100);
  }

  @Test
  @Order(6)
  public void updateQaIdNonexistent() {
    // 更新一个不存在的ID的QA ID
    String nonExistId = getNonExistId();
    Boolean result = mongoProjectDAO.updateQaId(nonExistId, 1L);
    Assertions.assertFalse(result);
  }

  @Test
  @Order(7)
  public void updateQaIdExistent() {
    // 更新一个存在的ID的QA ID
    for (int i = 0; i < projectIds.size(); i++) {
      String projectId = projectIds.get(i);
      Boolean result = mongoProjectDAO.updateQaId(projectId, i / 10L);
      Assertions.assertTrue(result);
    }
  }

  @Test
  @Order(8)
  public void countByQaIdNonexistent() {
    // 查找一个不存在的ID的QA ID
    long count = mongoProjectDAO.countByQaId(-1L);
    Assertions.assertEquals(0, count);
  }

  @Test
  @Order(9)
  public void countByQaIdExistent() {
    // 查找一个存在的ID的QA ID
    for (int i = 0; i < projectIds.size(); i++) {
      long count = mongoProjectDAO.countByQaId(i / 10L);
      Assertions.assertEquals(10, count);
    }
  }

  @Test
  @Order(10)
  public void findProjectsByQaIdNonexistent() {
    // 查找一个不存在的ID的QA ID
    List<ProjectOutline> projects = mongoProjectDAO.findProjectByQaId(-1L, 1, 10);
    Assertions.assertNotNull(projects);
    Assertions.assertEquals(0, projects.size());
  }

  @Test
  @Order(11)
  public void findProjectsByQaIdExistent() {
    // 查找一个存在的ID的QA ID
    for (int i = 0; i < projectIds.size(); i++) {
      List<ProjectOutline> projects = mongoProjectDAO.findProjectByQaId(i / 10L, 1, 10);
      Assertions.assertNotNull(projects);
      Assertions.assertEquals(10, projects.size());
    }
  }

  @Test
  @Order(12)
  public void countProjectsByMarketerIdNonexistent() {
    // 查找一个不存在的ID的Marketer ID
    long count = mongoProjectDAO.countByMarketerId(-1L);
    Assertions.assertEquals(0, count);
  }

  @Test
  @Order(13)
  public void countProjectsByMarketerIdExistent() {
    // 查找一个存在的ID的Marketer ID
    for (int i = 0; i < projectIds.size(); i++) {
      long count = mongoProjectDAO.countByMarketerId(i / 10L);
      Assertions.assertEquals(10, count);
    }
  }

  @Test
  @Order(14)
  public void findProjectsByMarketerIdNonexistent() {
    // 查找一个不存在的ID的Marketer ID
    List<ProjectOutline> projects = mongoProjectDAO.findProjectByMarketerId(-1L, 1, 10);
    Assertions.assertNotNull(projects);
    Assertions.assertEquals(0, projects.size());
  }

  @Test
  @Order(15)
  public void findProjectsByMarketerIdExistent() {
    // 查找一个存在的ID的Marketer ID
    for (int i = 0; i < projectIds.size(); i++) {
      List<ProjectOutline> projects = mongoProjectDAO.findProjectByMarketerId(i / 10L, 1, 10);
      Assertions.assertNotNull(projects);
      Assertions.assertEquals(10, projects.size());
    }
  }

  @Test
  @Order(16)
  public void countProjectsByTesterIdNonexistent() {
    // 查找一个不存在的ID的Tester ID
    long count = mongoProjectDAO.countByTesterId(-1L);
    Assertions.assertEquals(0, count);
  }

  @Test
  @Order(17)
  public void countProjectsByTesterIdExistent() {
    // 查找一个存在的ID的Tester ID
    for (int i = 0; i < projectIds.size(); i++) {
      long count = mongoProjectDAO.countByTesterId(i / 10L);
      Assertions.assertEquals(10, count);
    }
  }

  @Test
  @Order(18)
  public void findProjectsByTesterIdNonexistent() {
    // 查找一个不存在的ID的Tester ID
    List<ProjectOutline> projects = mongoProjectDAO.findProjectByTesterId(-1L, 1, 10);
    Assertions.assertNotNull(projects);
    Assertions.assertEquals(0, projects.size());
  }

  @Test
  @Order(19)
  public void findProjectsByTesterIdExistent() {
    // 查找一个存在的ID的Tester ID
    for (int i = 0; i < projectIds.size(); i++) {
      List<ProjectOutline> projects = mongoProjectDAO.findProjectByTesterId(i / 10L, 1, 10);
      Assertions.assertNotNull(projects);
      Assertions.assertEquals(10, projects.size());
    }
  }

  @Test
  @Order(20)
  public void updateFormIdNonexistent() {
    // 更新一个不存在的ID的Form ID
    String nonExistId = getNonExistId();
    Boolean result = mongoProjectDAO.updateFormIds(nonExistId, new ProjectFormIds());
    Assertions.assertFalse(result);
  }

  @Test
  @Order(21)
  public void updateFormIdExistent() {
    // 更新一个存在的ID的Form ID
    for (String projectId : projectIds) {
      ProjectFormIds formIds = new ProjectFormIds();
      Boolean result = mongoProjectDAO.updateFormIds(projectId, formIds);
      Assertions.assertTrue(result);
    }
  }

  @Test
  @Order(22)
  public void updateStatusNonexistent() {
    // 更新一个不存在的ID的Status
    String nonExistId = getNonExistId();
    boolean result = mongoProjectDAO.updateStatus(nonExistId, new ProjectStatus());
    Assertions.assertFalse(result);
  }

  @Test
  @Order(23)
  public void updateStatusExistent() {
    // 更新一个存在的ID的Status
    for (String projectId : projectIds) {
      ProjectStatus status = new ProjectStatus();
      boolean result = mongoProjectDAO.updateStatus(projectId, status);
      Assertions.assertTrue(result);
    }
  }

  @Test
  @Order(24)
  public void deleteProjectNonexistent() {
    // 删除一个不存在的ID的Project
    String nonExistId = getNonExistId();
    Boolean result = mongoProjectDAO.deleteProject(nonExistId);
    Assertions.assertFalse(result);
  }

  @Test
  @Order(25)
  public void deleteProjectExistent() {
    // 删除一个存在的ID的Project
    for (String projectId : projectIds) {
      Boolean result = mongoProjectDAO.deleteProject(projectId);
      Assertions.assertTrue(result);
    }
  }
}
