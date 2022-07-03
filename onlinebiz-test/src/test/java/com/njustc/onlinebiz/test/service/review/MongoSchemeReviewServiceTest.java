package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.review.SchemeReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewDAOFailureException;
import com.njustc.onlinebiz.test.exception.review.ReviewInvalidStageException;
import com.njustc.onlinebiz.test.exception.review.ReviewNotFoundException;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoSchemeReviewServiceTest {

    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final SchemeReviewDAO schemereviewDao = mock(SchemeReviewDAO.class);
    MongoSchemeReviewService schemereviewservice = new MongoSchemeReviewService(schemereviewDao, projectDAO);
    private final ProjectService projectService = mock(ProjectService.class);
    private final MultipartFile multipartfile = mock(MultipartFile.class);

    @Test
    void createSchemeReview() {

        SchemeReview schemereview = new SchemeReview();
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //分别赋予不同的ProjectId和TestId值，测试函数返回值结果是否符合预期
        schemereview.setId("TestId1");
        when(schemereviewDao.insertSchemeReview(any())).thenReturn(schemereview);
        assert(schemereviewservice.createSchemeReview("ProjectId1", "11",  3001L, 2001L).equals("TestId1"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId1"));
        schemereview.setId("TestId2022");
        assert(schemereviewservice.createSchemeReview("ProjectId2", "11", 3001L, 2001L).equals("TestId2022"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId2"));
        schemereview.setId("TestId321");
        assert(schemereviewservice.createSchemeReview("ProjectId3", "11", 3001L, 2001L).equals("TestId321"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId3"));
        schemereview.setId("TestId2023");
        assert(schemereviewservice.createSchemeReview("ProjectId4", "11", 3001L, 2001L).equals("TestId2023"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId4"));
    }

    @Test
    void findSchemeReview() {

        SchemeReview schemereview = new SchemeReview();
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());

        //开始测试
        //在阶段SCHEME_AUDITING（合法阶段）尝试查找测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 1001L, Role.MARKETER));
        //由市场部主管（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2066L, Role.TESTER));
        //由非指派的质量部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3066L, Role.QA));

        //由指派的测试部员工（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        //由测试部主管（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部人员（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        //由质量部主管（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        //由ADMIN（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));

        //尝试查找不存在的测试方案评审表
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(null);
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);

        //在阶段SCHEME_AUDITING_DENIED（合法阶段）尝试查找测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        //由客户（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 1001L, Role.MARKETER));
        //由市场部主管（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 1000L, Role.MARKETING_SUPERVISOR));
        //由非指派的测试部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2066L, Role.TESTER));
        //由非指派的质量部员工（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3066L, Role.QA));

        //由指派的测试部员工（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        //由测试部主管（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部人员（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        //由质量部主管（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        //由ADMIN（合法人员）查找测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));

        //尝试查找不存在的测试方案评审表
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(null);
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);

        //在阶段WAIT_FOR_QA（非法阶段）尝试查找测试方案评审表
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        //在阶段SCHEME_UNFILLED（非法阶段）尝试查找测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
    }

    @Test
    void updateSchemeReview() {

        SchemeReview schemereview = new SchemeReview();
        schemereview.setId("SchemeReviewId");
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        schemereview.setProjectId("ProjectId");
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);

        //开始测试
        //在阶段SCHEME_AUDITING（合法阶段）尝试修改测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        //由客户（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 1001L, Role.MARKETER));
        //由市场部主管（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 2001L, Role.TESTER));
        //由测试部主管（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 2000L, Role.TESTING_SUPERVISOR));
        //由非指派的质量部员工（非法人员）尝试修改测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3066L, Role.QA));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3003L, Role.QA));

        //由指派的质量部员工（合法人员）尝试修改测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3001L, Role.QA));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3001L, Role.QA));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);
        //由质量部主管（合法人员）尝试修改测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3000L, Role.QA_SUPERVISOR));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3000L, Role.QA_SUPERVISOR));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);
        //由ADMIN（合法人员）尝试修改测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 0L, Role.ADMIN));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 0L, Role.ADMIN));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);

        //尝试修改不存在的测试方案评审表
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(null);
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 0L, Role.ADMIN));
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);

        //尝试在WAIT_FOR_QA（非法阶段）修改测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 0L, Role.ADMIN));

        //尝试在WAIT_FOR_QA（非法阶段）修改测试方案评审表
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.updateSchemeReview("SchemeReviewId", schemereview, 0L, Role.ADMIN));
    }

    @Test
    void saveScannedCopy() {

        SchemeReview schemereview = new SchemeReview();
        schemereview.setId("SchemeReviewId");
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        schemereview.setProjectId("ProjectId");
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        when(schemereviewDao.updateScannedCopyPath(any(), any())).thenReturn(true);
        when(multipartfile.isEmpty()).thenReturn(false);

        //开始测试
        //尝试上传空的测试方案评审表扫描件
        when(multipartfile.isEmpty()).thenReturn(true);
        when(multipartfile.getOriginalFilename()).thenReturn("OriginalFilename.");
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.MARKETING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));
        when(multipartfile.isEmpty()).thenReturn(false);

        //在阶段WAIT_FOR_QA（非法阶段）下上传测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        when(multipartfile.isEmpty()).thenReturn(false);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.MARKETING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));

        //在阶段SCHEME_REVIEW_UPLOADED（非法阶段）下上传测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        when(multipartfile.isEmpty()).thenReturn(false);
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.MARKETING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));

        //在阶段SCHEME_AUDITING_PASSED（合法阶段）下上传测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
        when(multipartfile.isEmpty()).thenReturn(false);
        //由客户（非法人员）上传测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）上传测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 1001L, Role.MARKETER));
        //由市场部主管（非法人员）上传测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非法人员）上传测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2001L, Role.TESTER));
        //由非指派的质量部员工（非法人员）上传测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3066L, Role.QA));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3099L, Role.QA));

        //由市场部主管（合法人员）上传测试方案评审表扫描件
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.TESTING_SUPERVISOR));
        //由质量部主管（合法人员）上传测试方案评审表扫描件
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        //由指派的质量部员工（合法人员）上传测试方案评审表扫描件
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));
        //由ADMIN（合法人员）上传测试方案评审表扫描件
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));

        //尝试上传扫描文件名为空的测试方案评审表
        when(multipartfile.getOriginalFilename()).thenReturn(null);
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));
        when(multipartfile.getOriginalFilename()).thenReturn("OriginalFilename.");

        //若保存扫描路径失败
        when(schemereviewDao.updateScannedCopyPath(any(), any())).thenReturn(false);
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 3001L, Role.QA));
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.saveScannedCopy("SchemeReviewId", multipartfile, 0L, Role.ADMIN));
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);
    }

    @Test
    void getScannedCopy() {

        SchemeReview schemereview = new SchemeReview();
        schemereview.setId("SchemeReviewId");
        schemereview.setScannedCopyPath("null.null");
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
        schemereview.setProjectId("ProjectId");
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        when(schemereviewDao.updateSchemeReview(any(), any())).thenReturn(true);

        //开始测试
        //在状态WAIT_FOR_QA（非法阶段）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.WAIT_FOR_QA, ""));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));
        //在状态SCHEME_UNFILLED（非法状态）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));
        //在状态SCHEME_AUDITING（非法状态）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING, ""));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));
        //在状态SCHEME_AUDITING_DENIED（非法状态）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_DENIED, ""));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));
        //在状态SCHEME_AUDITING_PASSED（非法状态）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_AUDITING_PASSED, ""));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        Assertions.assertThrows(
                ReviewInvalidStageException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));

        //在状态REPORT_AUDITING（合法阶段）下下载测试方案评审表扫描件
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_REVIEW_UPLOADED, ""));
        //由客户（非法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 1001L, Role.MARKETER));
        //由市场部员工（非法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2001L, Role.TESTER));
        //由非指派的质量部员工（非法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3066L, Role.QA));
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3099L, Role.QA));

        //由指派的质量部员工（合法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                FileNotFoundException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3001L, Role.QA));
        //由测试部主管（合法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                FileNotFoundException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 2000L, Role.TESTING_SUPERVISOR));
        //由指派的质量部主管（合法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                FileNotFoundException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 3000L, Role.QA_SUPERVISOR));
        //由ADMIN（合法人员）尝试下载测试方案评审表扫描件
        Assertions.assertThrows(
                FileNotFoundException.class,
                () -> schemereviewservice.getScannedCopy("SchemeReviewId", 0L, Role.ADMIN));
    }

    @Test
    void removeSchemeReview() {

        SchemeReview schemereview = new SchemeReview();
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
        Project project = new Project();
        project.setStatus(new ProjectStatus(ProjectStage.SCHEME_UNFILLED, ""));
        when(projectDAO.findProjectById(any())).thenReturn(project);
        ProjectBaseInfo baseinfo = new ProjectBaseInfo();
        baseinfo.setMarketerId(1001L);
        baseinfo.setTesterId(2001L);
        baseinfo.setQaId(3001L);
        project.setProjectBaseInfo(baseinfo);
        when(projectService.getProjectBaseInfo(any())).thenReturn(project.getProjectBaseInfo());
        when(schemereviewDao.deleteSchemeAuditById(any())).thenReturn(true);

        //开始测试
        //由客户（非法人员）尝试删除测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 11111L, Role.CUSTOMER));
        //由市场部员工（非法人员）尝试删除测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 1001L, Role.MARKETER));
        //由市场部主管（非法人员）尝试删除测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 1000L, Role.MARKETING_SUPERVISOR));
        //由测试部员工（非法人员）尝试删除测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 2001L, Role.TESTER));
        //由测试部主管（非法人员）尝试删除测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 3000L, Role.TESTING_SUPERVISOR));

        //由质量部员工（合法人员）尝试删除测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.removeSchemeReview("SchemeReviewId", 3001L, Role.QA));
        //由质量部主管（合法人员）尝试删除测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.removeSchemeReview("SchemeReviewId", 2001L, Role.QA_SUPERVISOR));
        //由ADMIN（合法人员）尝试删除测试方案评审表
        Assertions.assertDoesNotThrow(() ->
                schemereviewservice.removeSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        when(schemereviewDao.deleteSchemeAuditById(any())).thenReturn(false);
        Assertions.assertThrows(
                ReviewDAOFailureException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        when(schemereviewDao.deleteSchemeAuditById(any())).thenReturn(false);

        //尝试删除不存在的测试方案评审表
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(null);
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> schemereviewservice.removeSchemeReview("SchemeReviewId", 0L, Role.ADMIN));
        when(schemereviewDao.findSchemeReviewById(any())).thenReturn(schemereview);
    }
}