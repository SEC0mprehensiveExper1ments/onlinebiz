package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Project;
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
        return projectService.createTestProject(userId, userRole);
    }

    @GetMapping("/test/{projectId}")
    public Project getProject(
            @PathVariable("projectId") String projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return projectService.findProject(projectId, userId, userRole);
    }


}
