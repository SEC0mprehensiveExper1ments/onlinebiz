package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.exception.TestPermissionDeniedException;
import com.njustc.onlinebiz.test.service.projectService.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping("/test")
    public String createTestProject(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        /*所有权限管理在controller层实现*/
        if(userRole!=Role.ADMIN && userRole != Role.MARKETER && userRole!=Role.MARKETING_SUPERVISOR)
            throw new TestPermissionDeniedException("无权新建测试项目");

        return projectService.createTestProject(userId, userRole);
    }


}
