package com.njustc.onlinebiz.test.controller;

import com.njustc.onlinebiz.test.model.Scheme;
import com.njustc.onlinebiz.test.service.schemeService.SchemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class SchemeController {
    private final SchemeService schemeService;

    public SchemeController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    // 查看任意测试方案的详细信息
    @GetMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Scheme> getSchemeById(@PathVariable("schemeId") String schemeId) {
        Scheme scheme = schemeService.findSchemeById(schemeId);
        return scheme == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(scheme);
    }

    // 查看用户（测试人员）生成的的所有测试方案
    @GetMapping("/test/scheme/individual")
    public ResponseEntity<List<Scheme>> getIndividualSchemes(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok().body(schemeService.search().byCreatorId(userId).getResult());
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
                schemeService.search()
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
                schemeService.updateScheme(scheme) ?
                        ResponseEntity.ok().build() :
                        ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/test/scheme/{schemeId}")
    public ResponseEntity<Void> deleteSchemeById(@PathVariable("schemeId") String schemeId) {
        return schemeService.removeScheme(schemeId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
