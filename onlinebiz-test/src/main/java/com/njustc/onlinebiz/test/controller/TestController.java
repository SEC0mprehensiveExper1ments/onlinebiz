package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.test.service.TestService;
import com.njustc.onlinebiz.common.model.Scheme;
import com.njustc.onlinebiz.common.model.ContractOutline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    private final TestService testService;

    @Autowired
    private RestTemplate restTemplate;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/test/scheme")
    public ResponseEntity<Scheme> createScheme(
            @RequestParam("applyId") Long applyId,
            @RequestParam("contractId") Long contractId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    // 查看任意测试方案的详细信息
    @GetMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Scheme> getSchemeById(@PathVariable("schemeId") String schemeId) {
        Scheme scheme = testService.findSchemeById(schemeId);
        return scheme == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(scheme);
    }

    // 查看用户自己的所有测试方案
    @GetMapping("/test/scheme/individual")
    public ResponseEntity<List<Scheme>> getIndividualTests(@RequestParam("userId") Long userId) {
        List<Scheme> schemes = null;
        List<ContractOutline> outlines = restTemplate.getForObject("/contract/individual/?id={userId}", List.class);
        if (outlines == null) {
            return null;
        }
        for (ContractOutline outline:outlines
             ) {
            schemes.addAll(testService.search().byContractId(outline.getId()).getResult());
        }
        return ResponseEntity.ok().body(schemes);
    }

    // 根据组合条件查询测试方案
    @GetMapping("/test/scheme/search")
    public ResponseEntity<List<Scheme>> searchTests(
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "representativeName", required = false) String representativeName,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "targetSoftware", required = false) String targetSoftware
    ) {
        // 检查条件是否全空
        if (contactName == null && companyName == null && representativeName == null &&
                projectName == null && targetSoftware == null) {
            return ResponseEntity.badRequest().build();
        }
        // null 参数会被忽略
        Map<String, String> map = new HashMap();
        map.put("contactName", contactName);
        map.put("companyName", companyName);
        map.put("representativeName", representativeName);
        map.put("projectName", projectName);
        map.put("targetSoftware", targetSoftware);

        List<ContractOutline> outlines = restTemplate.getForObject("/contract/search", List.class, map);
        List<Scheme> schemes = null;
        if (outlines == null) {
            return null;
        }
        for (ContractOutline outline:outlines
        ) {
            schemes.addAll(testService.search().byContractId(outline.getId()).getResult());
        }
        return ResponseEntity.ok().body(schemes);
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
