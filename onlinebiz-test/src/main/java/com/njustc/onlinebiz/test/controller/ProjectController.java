package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.TestProject;
import com.njustc.onlinebiz.test.service.schemeService.SchemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final SchemeService testService;

    public ProjectController(SchemeService testService) {
        this.testService = testService;
    }

    @PostMapping("/test")
    public ResponseEntity<TestProject> createTestProject(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        TestProject result = testService.createTestProject(userId);
        if (result != null) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().build();
    }


}
