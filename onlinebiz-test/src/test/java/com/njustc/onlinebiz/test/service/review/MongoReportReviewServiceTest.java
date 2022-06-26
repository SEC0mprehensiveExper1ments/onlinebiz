package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.review.ReportReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoReportReviewServiceTest {
  private final ReportReviewDAO reportReviewDAO = mock(ReportReviewDAO.class);
  private final ProjectDAO projectDAO = mock(ProjectDAO.class);
  private final MongoReportReviewService reportReviewService =
      new MongoReportReviewService(reportReviewDAO, projectDAO);

  @Test
  void createReportReviewByMarketer() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.insertReportReview(any())).thenReturn(reportReview);
    Assertions.assertDoesNotThrow(
        () -> reportReviewService.createReportReview("123", 1L, Role.MARKETER));
  }

  @Test
  void createReportReviewByTester() {
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> reportReviewService.createReportReview("123", 1L, Role.TESTER));
  }

  @Test
  void createReportReviewByQa() {
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> reportReviewService.createReportReview("123", 1L, Role.QA));
  }

  @Test
  void findReportReviewDaoNull() {
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReviewNotFoundException.class,
        () -> reportReviewService.findReportReview("123", 1L, Role.MARKETER));
  }

  @Test
  void findReportReviewByQaSupervisor() {
    ReportReview reportReview = new ReportReview();
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(
        () -> reportReviewService.findReportReview("123", 1L, Role.QA_SUPERVISOR));
  }

  @Test
  void findReportReviewByInvalidTester() {
    ReportReview reportReview = new ReportReview();
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(3L);
    baseInfo.setTesterId(2L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> reportReviewService.findReportReview("123", 1L, Role.TESTER));
  }

  @Test
  void findReportReviewInvalidStatus() {
    ReportReview reportReview = new ReportReview();
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(3L);
    baseInfo.setTesterId(2L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ReviewInvalidStageException.class,
        () -> reportReviewService.findReportReview("123", 3L, Role.QA));
  }

  @Test
  void findReportReviewByQa() {
    ReportReview reportReview = new ReportReview();
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_QA_DENIED, ""));
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(3L);
    baseInfo.setTesterId(2L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertDoesNotThrow(() -> reportReviewService.findReportReview("123", 3L, Role.QA));
  }

  @Test
  void updateReportReviewDifferentId() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> reportReviewService.updateReportReview("456", new ReportReview(), 1L, Role.ADMIN));
  }

  @Test
  void updateReportReviewByQaSupervisor() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(reportReviewDAO.updateReportReview(any(), any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () -> reportReviewService.updateReportReview("123", reportReview, 1L, Role.QA_SUPERVISOR));
  }

  @Test
  void updateReportReviewDaoFailure() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    when(projectDAO.findProjectById(any())).thenReturn(project);
    when(reportReviewDAO.updateReportReview(any(), any())).thenReturn(false);
    Assertions.assertThrows(
        ReviewDAOFailureException.class,
        () -> reportReviewService.updateReportReview("123", reportReview, 1L, Role.QA_SUPERVISOR));
  }

  @Test
  void updateReportReviewByInvalidTester() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.REPORT_AUDITING, ""));
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(3L);
    baseInfo.setTesterId(2L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> reportReviewService.updateReportReview("123", reportReview, 1L, Role.TESTER));
  }

  @Test
  void updateReportReviewInvalidStatus() {
    ReportReview reportReview = new ReportReview();
    reportReview.setId("123");
    when(reportReviewDAO.findReportReviewById(any())).thenReturn(reportReview);
    Project project = new Project();
    project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
    ProjectBaseInfo baseInfo = new ProjectBaseInfo();
    baseInfo.setQaId(3L);
    baseInfo.setTesterId(2L);
    project.setProjectBaseInfo(baseInfo);
    when(projectDAO.findProjectById(any())).thenReturn(project);
    Assertions.assertThrows(
        ReviewInvalidStageException.class,
        () -> reportReviewService.updateReportReview("123", reportReview, 3L, Role.QA));
  }

  @Test
  void saveScannedCopyEmpty() {
    MultipartFile file = mock(MultipartFile.class);
    when(file.isEmpty()).thenReturn(true);
    Assertions.assertThrows(
        ReviewNotFoundException.class,
        () -> reportReviewService.saveScannedCopy("", file, 1L, Role.ADMIN));
  }

  @Test
  void getScannedCopy() {}

  @Test
  void removeReportReview() {}
}
