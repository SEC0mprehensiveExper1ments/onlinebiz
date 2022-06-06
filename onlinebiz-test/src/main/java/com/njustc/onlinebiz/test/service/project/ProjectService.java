package com.njustc.onlinebiz.test.service.project;

import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.project.*;

/**
 * 测试项目服务层接口
 */
public interface ProjectService {
    /**
     * 创建一份测试项目。只有市场部人员和市场部主管可以执行此操作，此操作会设置测试方案的创建者ID，
     * 测试方案内容以及测试方案的初始状态。默认的初始状态是等待分配市场部员工。
     * @param userId 执行此操作的用户 ID
     * @param userRole 执行此操作的用户角色
     * @param entrustId 该测试项目对应的委托 ID
     * @return 成功返回测试方案ID，失败返回 null
     */
    String createTestProject(Long userId, Role userRole, String entrustId);

    /**
     * 查看一份测试项目的完整信息。所有后台管理人员均可以执行此操作。注意当质量部人员未分配时，无法获取project对象
     * @param projectId 要查看的测试方案 ID
     * @param userId 执行此操作的用户 ID
     * @param userRole 执行此操作的用户角色
     * @return 如果存在返回该测试方案对象，否则返回 null
     */
    Project findProject(String projectId, Long userId, Role userRole);

    /**
     * 查看与某个用户相关的所有测试项目列表。各个部门员工都只能看到他们自己的，主管和ADMIN可以看到所有的
     * 注意如果还未分配质量部人员，则无法得到该项目的Project的对象，此时可以通过findProjectOutlines可以查看
     * @param page 查询结果的页码
     * @param pageSize 每页有多少条记录
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 返回该用户可以看到的测试项目列表，是一个分页结果
     * */
    PageResult<ProjectOutline> findProjectOutlines(Integer page, Integer pageSize, Long userId, Role userRole);

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
     * 更改测试项目的状态，包括设置测试项目当前所处阶段和附加的说明信息.
     * @param projectId 要更改的测试项目ID
     * @param status 更改后的新状态
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateStatus(String projectId, ProjectStatus status, Long userId, Role userRole);

    /**
     * 获取测试项目的所有表单的 ID，内部接口，此接口内部不做权限检查，如有需要，请在调用前完成权限检查
     * @param projectId 待获取的测试项目ID
     * @return 返回测试项目的表单对象，否则返回null
     * */
    ProjectFormIds getProjectFormIds(String projectId);

    /**
     * 获取测试项目的基本信息，内部接口，此接口内部不做权限检查，如有需要，请在调用前完成权限检查
     * @param projectId 待获取的测试项目ID
     * @return 返回测试项目的基本信息对象，否则返回null
     * */
    ProjectBaseInfo getProjectBaseInfo(String projectId);
}
