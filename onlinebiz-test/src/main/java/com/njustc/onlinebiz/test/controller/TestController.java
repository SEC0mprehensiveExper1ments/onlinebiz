package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.test.service.TestService;
import com.njustc.onlinebiz.common.model.Scheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/test/scheme")
    public ResponseEntity<Scheme> createScheme(
            @RequestParam("userId") Long creatorId,
            @RequestParam("applyId") String applyId,
            @RequestBody Scheme scheme
    ) {
        Scheme result = testService.createScheme(creatorId, applyId, scheme);
        if (result != null) {
            return ResponseEntity.ok().body(scheme);
        }
        return ResponseEntity.badRequest().build();
    }

    // 查看任意测试方案的详细信息
    @GetMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Scheme> getSchemeById(@PathVariable("schemeId") String schemeId) {
        Scheme scheme = testService.findSchemeById(schemeId);
        return scheme == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(scheme);
    }

    // 查看用户（测试人员）生成的的所有测试方案
    @GetMapping("/test/scheme/individual")
    public ResponseEntity<List<Scheme>> getIndividualSchemes(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(testService.search().byCreatorId(userId).getResult());
    }

    // 根据组合条件查询测试方案
    @GetMapping("/test/scheme/search")
    public ResponseEntity<List<Scheme>> searchSchemes(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware
    ) {
        // 检查条件是否全空
        if (contactName == null && companyName == null && targetSoftware == null) {
            return ResponseEntity.badRequest().build();
        }
        // null 参数会被忽略
        return ResponseEntity.ok().body(
                testService.search()
                        .byContact(contactName)
                        .byCompany(companyName)
                        .byProjectName(projectName)
                        .byTargetSoftware(targetSoftware)
                        .getResult()
        );
    }

    @PostMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Void> updateSchemeById(
            @PathVariable("schemeId") String schemeId,
            @RequestBody Scheme scheme
    ) {
        return schemeId.equals(scheme.getId()) ?
                testService.updateScheme(scheme) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Void> deleteSchemeById(@PathVariable("schemeId") String schemeId) {
        return testService.removeScheme(schemeId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
