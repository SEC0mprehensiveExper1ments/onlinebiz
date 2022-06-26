package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.test.dao.review.EntrustTestReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoEntrustTestReviewServiceTest {
  private final EntrustTestReviewDAO entrustTestReviewDAO = mock(EntrustTestReviewDAO.class);
  private final MongoEntrustTestReviewService entrustTestReviewService =
      new MongoEntrustTestReviewService(entrustTestReviewDAO);

  @Test
  void createEntrustTestReviewByCustomer() {
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> entrustTestReviewService.createEntrustTestReview("projectId", 1L, Role.CUSTOMER));
  }

  @Test
  void createEntrustTestReviewByTester() {
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> entrustTestReviewService.createEntrustTestReview("projectId", 1L, Role.TESTER));
  }

  @Test
  void createEntrustTestReviewByQa() {
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () ->
            entrustTestReviewService.createEntrustTestReview("projectId", 1L, Role.QA_SUPERVISOR));
  }

  @Test
  void createEntrustTestReviewByMarketer() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    entrustTestReview.setId("id");
    when(entrustTestReviewDAO.insertEntrustTestReview(any(EntrustTestReview.class)))
        .thenReturn(entrustTestReview);
    Assertions.assertDoesNotThrow(
        () -> entrustTestReviewService.createEntrustTestReview("projectId", 1L, Role.MARKETER));
  }

  @Test
  void findEntrustTestReviewDaoNull() {
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReviewNotFoundException.class,
        () -> entrustTestReviewService.findEntrustTestReview("projectId", 1L, Role.MARKETER));
  }

  @Test
  void findEntrustTestReviewByCustomer() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(entrustTestReview);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> entrustTestReviewService.findEntrustTestReview("projectId", 1L, Role.CUSTOMER));
  }

  @Test
  void findEntrustTestReviewByAnyWorker() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(entrustTestReview);
    Assertions.assertDoesNotThrow(
        () -> entrustTestReviewService.findEntrustTestReview("projectId", 1L, Role.TESTER));
    Assertions.assertDoesNotThrow(
        () -> entrustTestReviewService.findEntrustTestReview("projectId", 1L, Role.QA_SUPERVISOR));
    Assertions.assertDoesNotThrow(
        () -> entrustTestReviewService.findEntrustTestReview("projectId", 1L, Role.MARKETER));
  }

  @Test
  void updateEntrustTestReviewDaoNull() {
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReviewNotFoundException.class,
        () ->
            entrustTestReviewService.updateEntrustTestReview(
                "projectId", new EntrustTestReview(), 1L, Role.MARKETER));
  }

  @Test
  void updateEntrustTestReviewBelongsToDifferentProject() {
    EntrustTestReview originEntrustTestReview = new EntrustTestReview();
    originEntrustTestReview.setProjectId("originProjectId");
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(originEntrustTestReview);
    EntrustTestReview newEntrustTestReview = new EntrustTestReview();
    newEntrustTestReview.setProjectId("newProjectId");
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () ->
            entrustTestReviewService.updateEntrustTestReview(
                "newProjectId", newEntrustTestReview, 1L, Role.MARKETER));
  }

  @Test
  void updateEntrustTestReviewByCustomer() {
    EntrustTestReview originEntrustTestReview = new EntrustTestReview();
    originEntrustTestReview.setProjectId("projectId");
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(originEntrustTestReview);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () ->
            entrustTestReviewService.updateEntrustTestReview(
                "projectId", originEntrustTestReview, 1L, Role.CUSTOMER));
  }

  @Test
  void updateEntrustTestReviewDaoFailure() {
    EntrustTestReview originEntrustTestReview = new EntrustTestReview();
    originEntrustTestReview.setProjectId("projectId");
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(originEntrustTestReview);
    when(entrustTestReviewDAO.updateEntrustTestReview(any(), any())).thenReturn(false);
    Assertions.assertThrows(
        ReviewDAOFailureException.class,
        () ->
            entrustTestReviewService.updateEntrustTestReview(
                "projectId", originEntrustTestReview, 1L, Role.MARKETER));
  }

  @Test
  void updateEntrustTestReviewSuccess() {
    EntrustTestReview originEntrustTestReview = new EntrustTestReview();
    originEntrustTestReview.setProjectId("projectId");
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(originEntrustTestReview);
    when(entrustTestReviewDAO.updateEntrustTestReview(any(), any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () ->
            entrustTestReviewService.updateEntrustTestReview(
                "projectId", originEntrustTestReview, 1L, Role.MARKETER));
  }

  @Test
  void removeEntrustTestReviewDaoNull() {
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(null);
    Assertions.assertThrows(
        ReviewNotFoundException.class,
        () -> entrustTestReviewService.removeEntrustTestReview("projectId", 1L, Role.MARKETER));
  }

  @Test
  void removeEntrustTestReviewNotByAdmin() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(entrustTestReview);
    Assertions.assertThrows(
        ReviewPermissionDeniedException.class,
        () -> entrustTestReviewService.removeEntrustTestReview("projectId", 1L, Role.CUSTOMER));
  }

  @Test
  void removeEntrustTestReviewDaoFailure() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(entrustTestReview);
    when(entrustTestReviewDAO.deleteEntrustTestReviewById(any())).thenReturn(false);
    Assertions.assertThrows(
        ReviewDAOFailureException.class,
        () -> entrustTestReviewService.removeEntrustTestReview("projectId", 1L, Role.ADMIN));
  }

  @Test
  void removeEntrustTestReviewSuccess() {
    EntrustTestReview entrustTestReview = new EntrustTestReview();
    when(entrustTestReviewDAO.findEntrustTestReviewById(any())).thenReturn(entrustTestReview);
    when(entrustTestReviewDAO.deleteEntrustTestReviewById(any())).thenReturn(true);
    Assertions.assertDoesNotThrow(
        () -> entrustTestReviewService.removeEntrustTestReview("projectId", 1L, Role.ADMIN));
  }
}
