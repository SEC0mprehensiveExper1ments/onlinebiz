package com.njustc.onlinebiz.test.service.review;

import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.test.dao.review.SchemeReviewDAO;
import org.junit.jupiter.api.Test;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectBaseInfo;
import com.njustc.onlinebiz.common.model.test.project.ProjectStage;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.dao.project.ProjectDAO;
import com.njustc.onlinebiz.test.dao.testrecord.TestRecordDAO;
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
        //
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
        //
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