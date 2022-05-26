package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.scheme.SchemeNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import com.njustc.onlinebiz.test.model.scheme.Modification;
import com.njustc.onlinebiz.test.model.scheme.Schedule;
import com.njustc.onlinebiz.test.model.scheme.SchemeContent;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class MongoSchemeServiceTest {

    @Autowired
    private SchemeService schemeService;
    private static String schemeId1 = null;
    private static String schemeId2 = null;
    private static String schemeId3 = null;

    @Test
    void updateScheme() {
        SchemeContent content = new SchemeContent();
        content.setVersion("版本号");
        Modification modification = new Modification();
        modification.setVersion("版本号");
        modification.setDate(new Date(2020, 10, 2));
        modification.setMethod(Modification.Method.A);
        modification.setModifier("修订者");
        modification.setIllustration("说明");
        List<Modification> modificationList = new ArrayList<>();
        modificationList.add(modification);
        content.setModificationList(modificationList);
        content.setLogo("标识");
        content.setSystemSummary("系统概述");
        content.setDocumentSummary("文档概述");
        content.setBaseline("基线");
        content.setHardware("硬件");
        content.setSoftware("软件");
        content.setOtherEnvironment("其他");
        content.setOrganization("参与组织");
        content.setParticipant("人员");
        content.setTestLevel("测试级别");
        content.setTestType("测试类别");
        content.setTestCondition("一般测试条件");
        content.setTestToBeExecuted("计划执行的测试");
        content.setTestSample("测试用例");
        Schedule schedule = new Schedule();
        schedule.setWorkload("工作量");
        schedule.setStartDate(new Date(2020, 10, 2));
        schedule.setEndDate(new Date(2020, 10, 2));
        content.setPlanSchedule(schedule);
        content.setDesignSchedule(schedule);
        content.setExecuteSchedule(schedule);
        content.setEvaluateSchedule(schedule);
        content.setTraceability("需求的可追踪性");
        //由质量部（非合法人员）更新测试方案
        try {
            schemeService.updateScheme("628cd123fc3e4a7d4fa0df7e", content, 1L, Role.QA);
        } catch (Exception e) {
            assert e.getClass().equals(SchemePermissionDeniedException.class);
        }
        //由测试部（合法人员）更新测试方案
        schemeService.updateScheme("628cd123fc3e4a7d4fa0df7e", content, 5L, Role.TESTER);
        schemeService.updateScheme("628cd123fc3e4a7d4fa0df7e", content, 6L, Role.TESTING_SUPERVISOR);
        //更新不存在的测试方案
        try {
            schemeService.updateScheme("abc", content, 5L, Role.TESTER);
        } catch (Exception e) {
            assert e.getClass().equals(SchemeNotFoundException.class);
        }
    }

    @Test
    void denyContent() {
    }

    @Test
    void approveContent() {
    }

    @Test
    void removeScheme() {
    }

    @Test
    void findScheme() {
    }
}