package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;

import java.util.List;

/**
 * testcase服务
 *
 */
public interface TestcaseService {

    /**
     * 创建一份测试用例表
     *
     * @param projectId 项目id
     * @param entrustId 委托id
     * @param testcases 测试点
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link String}
     */
    String createTestcaseList(String projectId, String entrustId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole);

    /**
     * 根据测试用例表id查找相应测试用例表
     *
     * @param testcaseListId testcase id列表
     * @param userId 用户id
     * @param userRole 用户角色
     * @return {@link Testcase}
     */
    Testcase findTestcaseList(String testcaseListId, Long userId, Role userRole);

    /**
     * 更新测试用例表信息
     *
     * @param testcaseListId testcase id列表
     * @param testcases 测试点
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void updateTestcaseList(String testcaseListId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole);

    /**
     * 根据测试用例表id删除测试用例表
     *
     * @param testcaseListId testcase id列表
     * @param userId 用户id
     * @param userRole 用户角色
     */
    void removeTestcaseList(String testcaseListId, Long userId, Role userRole);
}
