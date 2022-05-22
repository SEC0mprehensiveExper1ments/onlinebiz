package com.njustc.onlinebiz.test.controller.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Scheme;
import com.njustc.onlinebiz.test.model.SchemeContent;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
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
    public Scheme getScheme(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return schemeService.findScheme(schemeId, userId, userRole);
    }

    // 修改测试方案信息，并将该测试方案状态标记为提交待审核
    @PostMapping("/test/scheme/{schemeId}/content")
    public void updateContent(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody SchemeContent content
    ) {
        schemeService.updateScheme(schemeId, content, userId, userRole);
    }

    // 拒绝测试方案
    @PostMapping("/test/scheme/{schemeId}/content/denial")
    public void denyContent(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam(value = "message", required = false) String message
    ) {
        schemeService.denyContent(schemeId, message, userId, userRole);
    }

    // 接受测试方案
    @PostMapping("/test/scheme/{schemeId}/content/acceptance")
    public void denyContent(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        schemeService.approveContent(schemeId, userId, userRole);
    }

    // 删除一份测试方案
    @DeleteMapping("/test/scheme/{schemeId}")
    public void removeScheme(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        schemeService.removeScheme(schemeId, userId, userRole);
    }
}
