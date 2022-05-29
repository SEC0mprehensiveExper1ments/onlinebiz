package com.njustc.onlinebiz.test.controller.testissue;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.test.service.testissue.TestIssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestIssueController {
    private final TestIssueService testIssueService;

    public TestIssueController(TestIssueService testIssueService) {
        this.testIssueService = testIssueService;
    }

    // 查看任意测试问题清单的详细信息
    @GetMapping("/test/testIssue/{testIssueId}")
    public TestIssueList getTestIssue(
            @PathVariable("testIssueId") String testIssueId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return testIssueService.findTestIssueList(testIssueId, userId, userRole);
    }

    // 修改测试问题清单
    @PostMapping("/test/testIssue/{testIssueId}/content")
    public void updateContent(
            @PathVariable("testIssueId") String testIssueId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody List<TestIssueList.TestIssue> content
    ) {
        testIssueService.updateTestIssueList(testIssueId, content, userId, userRole);
    }

    // 删除一份测试问题清单
    @DeleteMapping("/test/testIssue/{testIssueId}")
    public void removeTestIssue(
            @PathVariable("testIssueId") String testIssueId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        testIssueService.removeTestIssueList(testIssueId, userId, userRole);
    }
}
