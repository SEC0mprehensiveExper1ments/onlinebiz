package com.njustc.onlinebiz.test.controller.testcase;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.test.service.testcase.TestcaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * testcase控制器
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TestcaseController {
    private final TestcaseService testcaseService;

    public TestcaseController(TestcaseService testcaseService) {
        this.testcaseService = testcaseService;
    }

    /**
     * 查看任意测试用例表的详细信息
     *
     * @param testcaseId testcase id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Testcase}
     */
    @GetMapping("/test/testcase/{testcaseId}")
    public Testcase getTestcase(
            @PathVariable("testcaseId") String testcaseId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return testcaseService.findTestcaseList(testcaseId, userId, userRole);
    }

    /**
     * 修改测试用例表
     *
     * @param testcaseId testcase id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param content 内容
     */
    @PostMapping("/test/testcase/{testcaseId}/content")
    public void updateContent(
            @PathVariable("testcaseId") String testcaseId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody List<Testcase.TestcaseList> content
    ) {
        testcaseService.updateTestcaseList(testcaseId, content, userId, userRole);
    }

    /**
     * 删除一份测试用例表
     *
     * @param testcaseId testcase id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    @DeleteMapping("/test/testcase/{testcaseId}")
    public void removeTestcase(
            @PathVariable("testcaseId") String testcaseId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        testcaseService.removeTestcaseList(testcaseId, userId, userRole);
    }
}
