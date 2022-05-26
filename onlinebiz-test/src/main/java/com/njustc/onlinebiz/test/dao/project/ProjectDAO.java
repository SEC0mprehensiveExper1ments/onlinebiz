package com.njustc.onlinebiz.test.dao.project;

import com.njustc.onlinebiz.test.model.project.Project;

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
     * 删除指定的测试项目
     * @param projectId 要删除的项目ID
     * @return 成功返回 true，失败返回 false
     * **/
    Boolean deleteProject(String projectId);
}
