package com.njustc.onlinebiz.test.controller.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Scheme;
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
}
