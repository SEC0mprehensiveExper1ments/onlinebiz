package com.njustc.onlinebiz.test.dao.testcase;

import com.njustc.onlinebiz.common.model.test.testcase.Testcase;

import java.util.List;

/**
 * 测试用例dao层接口
 *
 */
public interface TestcaseDAO {
    /**
     * 插入testcase列表
     *
     * @param testcaseList testcase列表
     * @return {@link Testcase}
     */
    Testcase insertTestcaseList(Testcase testcaseList);

    /**
     * 找到testcase通过id列表
     *
     * @param testcaseListId testcase id列表
     * @return {@link Testcase}
     */
    Testcase findTestcaseListById(String testcaseListId);

    /**
     * 更新内容
     *
     * @param testcaseListId testcase id列表
     * @param content 内容
     * @return boolean
     */
    boolean updateContent(String testcaseListId, List<Testcase.TestcaseList> content);

    /**
     * 删除testcase列表
     *
     * @param testcaseListId testcase id列表
     * @return boolean
     */
    boolean deleteTestcaseList(String testcaseListId);
}
