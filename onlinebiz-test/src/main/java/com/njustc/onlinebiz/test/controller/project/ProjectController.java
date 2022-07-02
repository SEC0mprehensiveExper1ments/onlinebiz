package com.njustc.onlinebiz.test.controller.project;

import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectOutline;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;
import com.njustc.onlinebiz.test.service.project.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 项目控制器
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 创建测试项目，返回测试项目ID
     *
     * @param userId 用户id
     * @param userRole 用户角色
     * @param entrustId 委托id
     * @return {@link String}
     */
    @PostMapping("/test")
    public String createTestProject(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("entrustId") String entrustId
            ) {
        return projectService.createTestProject(userId, userRole, entrustId);
    }

    /**
     * 获取测试项目的基本信息列表
     *
     * @param userId 用户id
     * @param userRole 用户角色
     * @param page 页面
     * @param pageSize 页面大小
     * @return {@link PageResult<ProjectOutline>}
     */
    @GetMapping("/test")
    public PageResult<ProjectOutline> getProjects(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize
    ) {
        return projectService.findProjectOutlines(page, pageSize, userId, userRole);
    }

    /**
     * 获取项目的完整信息
     *
     * @param projectId 项目id
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Project}
     */
    @GetMapping("/test/{projectId}")
    public Project getProject(
            @PathVariable("projectId") String projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        return projectService.findProject(projectId, userId, userRole);
    }

    /**
     * 分配质量部人员 (成功分配质量部人员后，项目会进入到 进行中 状态)
     *
     * @param projectId 项目id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param qaId qa id
     */
    @PostMapping("/test/{projectId}/qa")
    public void allocateQa(
            @PathVariable("projectId") String projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestParam("qaId") Long qaId
    ) {
        projectService.updateQa(projectId, qaId, userId, userRole);
    }


    /**
     * 更新项目状态，后端会检查状态更新的合法性
     *
     * @param projectId 项目id
     * @param userId 用户id
     * @param userRole 用户角色
     * @param status 状态
     */
    @PostMapping("/test/{projectId}/status")
    public void updateStatus(
            @PathVariable("projectId") String projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole,
            @RequestBody ProjectStatus status
    ) {
        projectService.updateStatus(projectId, status, userId, userRole);
    }

    /**
     * 删除一份项目 (一般情况不需要)
     *
     * @param projectId 项目id
     * @param userId 用户id
     * @param userRole 用户角色
     */
    @DeleteMapping("/test/{projectId}")
    public void removeProject(
            @PathVariable("projectId") String projectId,
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") Role userRole
    ) {
        projectService.removeProject(projectId, userId, userRole);
    }
}
