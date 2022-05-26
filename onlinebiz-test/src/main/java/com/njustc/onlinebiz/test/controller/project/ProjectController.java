package com.njustc.onlinebiz.test.controller.project;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.project.Project;
import com.njustc.onlinebiz.test.service.project.ProjectService;
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
            @RequestParam("userRole") Role userRole,
            @RequestParam("entrustId") String entrustId
    ) {
        return projectService.createTestProject(userId, userRole, entrustId);
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
