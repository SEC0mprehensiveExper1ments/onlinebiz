package com.njustc.onlinebiz.test.service.projectService;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Project;

/**
 * 测试项目服务层接口
 */
public interface ProjectService {
    /**
     * 创建一份测试项目。只有市场部人员和市场部主管可以执行此操作，此操作会设置测试方案的创建者ID，
     * 测试方案内容以及测试方案的初始状态。默认的初始状态是未完成。
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回测试方案ID，失败返回 null
     */
    String createTestProject(Long userId, Role userRole);

    /**
     * 查看一份测试项目的完整信息。所有后台管理人员均可以执行此操作。
     * @param projectId 要查看的测试方案ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 如果存在返回该测试方案对象，否则返回 null
     */
    Project findProject(String projectId, Long userId, Role userRole);
}
