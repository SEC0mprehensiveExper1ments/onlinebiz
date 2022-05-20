package com.njustc.onlinebiz.test.service.projectService;

import com.njustc.onlinebiz.common.model.Role;

/**
 * 测试项目服务层接口
 */
public interface ProjectService {
    /**
     * 创建一份测试项目。只有市场部人员和市场部主管可以执行此操作，此操作会设置委托的创建者ID，
     * 委托内容以及委托的初始状态。默认的初始状态是等待分配市场部人员。
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回委托ID，失败返回 null
     */
    String createTestProject(Long userId, Role userRole);
}
