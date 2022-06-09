package com.njustc.onlinebiz.test.dao.project;

import com.njustc.onlinebiz.common.model.test.project.Project;
import com.njustc.onlinebiz.common.model.test.project.ProjectFormIds;
import com.njustc.onlinebiz.common.model.test.project.ProjectOutline;
import com.njustc.onlinebiz.common.model.test.project.ProjectStatus;

import java.util.List;

/**
 * 测试项目整体模块的数据访问层接口
 * **/
public interface ProjectDAO {

    /**
     * 保存一份新的测试项目到数据库
     * @param project 测试项目实体
     * @return 保存后的测试项目实体
     */
    Project insertProject(Project project);

    /**
     * 根据测试项目 ID 查询测试项目实体
     * @param projectId 要查询的测试项目 ID
     * @return 查询得到的测试项目实体, 不存在则返回null
     */
    Project findProjectById(String projectId);

    /**
     * 更新测试项目的质量部人员ID
     * @param projectId 要更新的测试项目ID
     * @param qaId 更改后的质量部人员ID
     * @return 成功返回true，失败返回false
     * **/
    Boolean updateQaId(String projectId, Long qaId);

    /**
     * 更新测试项目的表单ids
     * @param projectId 要更新的测试项目ID
     * @param projectFormIds 更新的表单ids
     * @return 成功返回true，失败返回false
     * */
    Boolean updateFormIds(String projectId, ProjectFormIds projectFormIds);

    /**
     * 删除指定的测试项目
     * @param projectId 要删除的项目ID
     * @return 成功返回 true，失败返回 false
     * **/
    Boolean deleteProject(String projectId);

    /**
     * 修改测试项目的状态
     * @param projectId 委托ID
     * @param status 修改后的状态
     * @return 成功返回 true，失败返回 false
     */
    boolean updateStatus(String projectId, ProjectStatus status);

    /**
     * 获取总项目数，主要配合分页查询使用
     * @return 总的项目数目
     */
    long countAll();

    /**
     * 查询所有测试项目的基本信息，允许设置分页页码和每页记录条数，页码从1开始
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的测试项目基本信息
     */
    List<ProjectOutline> findAllProjects(Integer page, Integer pageSize);


    /**
     * 获取某个市场部员工的总项目数目
     * @param marketerId 市场部员工ID
     * @return 该市场部员工的委托数
     */
    long countByMarketerId(Long marketerId);

    /**
     * 根据市场部员工ID查询概要列表，允许设置分页页码和每页记录条数，页码从1开始
     * @param marketerId 要查询的客户ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     */
    List<ProjectOutline> findProjectByMarketerId(Long marketerId, Integer page, Integer pageSize);

    /**
     * 获取某个测试部员工的总项目数目
     * @param testerId 测试部员工ID
     * @return 该测试部员工的委托数
     */
    long countByTesterId(Long testerId);

    /**
     * 根据测试部人员ID查询项目基本信息，允许设置分页页码和每页记录条数，页码从1开始
     * @param testerId 要查询的测试部人员ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的项目基本信息
     */
    List<ProjectOutline> findProjectByTesterId(Long testerId, Integer page, Integer pageSize);

    /**
     * 获取某个质量部员工的总项目数目
     * @param qaId 质量部员工ID
     * @return 该质量部员工的委托数
     */
    long countByQaId(Long qaId);

    /**
     * 根据质量部人员ID查询项目基本信息，允许设置分页页码和每页记录条数，页码从1开始
     * @param qaId 要查询的质量部人员ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的项目基本信息
     */
    List<ProjectOutline> findProjectByQaId(Long qaId, Integer page, Integer pageSize);
}
