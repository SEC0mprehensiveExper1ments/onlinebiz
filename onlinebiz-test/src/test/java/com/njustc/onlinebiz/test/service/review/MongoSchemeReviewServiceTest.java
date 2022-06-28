package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.test.dao.review.SchemeReviewDAO;
import com.njustc.onlinebiz.test.exception.review.ReviewPermissionDeniedException;
import com.njustc.onlinebiz.test.exception.testrecord.TestRecordPermissionDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import com.njustc.onlinebiz.common.model.Role;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class MongoSchemeReviewServiceTest {

    private final ProjectDAO projectDAO = mock(ProjectDAO.class);
    private final SchemeReviewDAO schemereviewDao = mock(SchemeReviewDAO.class);
    MongoSchemeReviewService schemereviewservice = new MongoSchemeReviewService(schemereviewDao, projectDAO);
    private final ProjectBaseInfo projectbaseinfo = mock(ProjectBaseInfo.class);
    private final ProjectService projectService = mock(ProjectService.class);
    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";

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
        assert(schemereviewservice.createSchemeReview("ProjectId1", 3001L, 2001L).equals("TestId1"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId1"));
        schemereview.setId("TestId2022");
        assert(schemereviewservice.createSchemeReview("ProjectId2", 3001L, 2001L).equals("TestId2022"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId2"));
        schemereview.setId("TestId321");
        assert(schemereviewservice.createSchemeReview("ProjectId3", 3001L, 2001L).equals("TestId321"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId3"));
        schemereview.setId("TestId2023");
        assert(schemereviewservice.createSchemeReview("ProjectId4", 3001L, 2001L).equals("TestId2023"));
        //assert(schemereviewDao.insertSchemeReview(any()).getProjectId().equals("ProjectId4"));
    }

    @Test
    void findSchemeReview() {

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
        //由客户（非法人员）查找测试方案评审表
        Assertions.assertThrows(
                ReviewPermissionDeniedException.class,
                () -> schemereviewservice.findSchemeReview("SchemeReviewId", 11111L, Role.CUSTOMER));
    }

    @Test
    void updateSchemeReview() {

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
        //
    }

    @Test
    void saveScannedCopy() {
    }

    @Test
    void getScannedCopy() {
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

        //开始测试
        //
    }
}