package com.njustc.onlinebiz.test.controller.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;
import com.njustc.onlinebiz.test.service.scheme.SchemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 测试方案控制器
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SchemeController {

    private final SchemeService schemeService;

    public SchemeController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    /**
     * 查看任意测试方案的详细信息
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Scheme}
     */
    @GetMapping("/test/scheme/{schemeId}")
    public Scheme getScheme(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole) {
        return schemeService.findScheme(schemeId, userId, userRole);
    }

    /**
     * 修改测试方案信息，并将该测试方案状态标记为提交待审核
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param content 内容
     */
    @PostMapping("/test/scheme/{schemeId}/content")
    public void updateContent(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody SchemeContent content
    ) {
        schemeService.updateScheme(schemeId, content, userId, userRole);
    }

    /**
     * 删除一份测试方案
     *
     * @param schemeId 计划id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    @DeleteMapping("/test/scheme/{schemeId}")
    public void removeScheme(
            @PathVariable("schemeId") String schemeId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        schemeService.removeScheme(schemeId, userId, userRole);
    }
}
