package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.project.Project;
import com.njustc.onlinebiz.test.model.project.ProjectStatus;

/**
 * 测试项目服务层接口
 */
public interface ProjectService {
    /**
     * 创建一份测试项目。只有市场部人员和市场部主管可以执行此操作，此操作会设置测试方案的创建者ID，
     * 测试方案内容以及测试方案的初始状态。默认的初始状态是等待分配市场部员工。
     * @param userId 执行此操作的用户 ID
     * @param userRole 执行此操作的用户角色
     * @param entrustId 该测试项目对应的委托申请 ID
     * @param marketerId 测试项目指定的市场部人员 ID
     * @param testerId 测试项目指定的测试部人员 ID
     * @return 成功返回测试方案ID，失败返回 null
     */
    String createTestProject(Long userId, Role userRole, String entrustId, Long marketerId, Long testerId);

    /**
     * 查看一份测试项目的完整信息。所有后台管理人员均可以执行此操作。
     * @param projectId 要查看的测试方案 ID
     * @param userId 执行此操作的用户 ID
     * @param userRole 执行此操作的用户角色
     * @return 如果存在返回该测试方案对象，否则返回 null
     */
    Project findProject(String projectId, Long userId, Role userRole);

    /**
     * 更新测试项目对应的质量部人员，只有管理员和质量部主管可以执行此操作。其中质量部主管只能在
     * 指定阶段执行此操作。
     * @param projectId 要更新的委托ID
     * @param qaId 对应的市场部人员ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateQa(String projectId, Long qaId, Long userId, Role userRole);

    /**
     * 删除一个测试项目，此接口正常流程不会使用(仅调试)，只有管理员可操作
     * @param projectId 要删除的测试项目ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * */
    void removeProject(String projectId, Long userId, Role userRole);

    /**
     * 更改测试项目的状态，包括设置测试项目当前所处阶段和附加的说明信息。只有管理员可以执行此操作。
     * @param projectId 要更改的测试项目ID
     * @param status 更改后的新状态
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateStatus(String projectId, ProjectStatus status, Long userId, Role userRole);
}
