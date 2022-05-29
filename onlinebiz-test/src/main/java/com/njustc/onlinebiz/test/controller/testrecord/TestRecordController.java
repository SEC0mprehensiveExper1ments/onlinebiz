package com.njustc.onlinebiz.test.controller.testrecord;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.test.service.testrecord.TestRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestRecordController {
    private final TestRecordService testRecordService;

    public TestRecordController(TestRecordService testRecordService) {
        this.testRecordService = testRecordService;
    }

    // 查看任意测试记录表的详细信息
    @GetMapping("/test/testRecord/{testRecordId}")
    public TestRecordList getTestRecord(
            @PathVariable("testRecordId") String testRecordId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return testRecordService.findTestRecordList(testRecordId, userId, userRole);
    }

    // 修改测试记录表
    @PostMapping("/test/testRecord/{testRecordId}/content")
    public void updateContent(
            @PathVariable("testRecordId") String testRecordId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody List<TestRecordList.TestRecord> content
    ) {
        testRecordService.updateTestRecordList(testRecordId, content, userId, userRole);
    }

    // 删除一份测试记录表
    @DeleteMapping("/test/testRecord/{testRecordId}")
    public void removeTestRecord(
            @PathVariable("testRecordId") String testRecordId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        testRecordService.removeTestRecordList(testRecordId, userId, userRole);
    }
}
