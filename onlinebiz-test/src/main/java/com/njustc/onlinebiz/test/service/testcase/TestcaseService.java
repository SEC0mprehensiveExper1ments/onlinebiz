package com.njustc.onlinebiz.test.service.testcase;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.Testcase;

import java.util.List;

public interface TestcaseService {

    //创建一份测试用例表
    String createTestcaseList(String entrustId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole);

    //根据测试用例表id查找相应测试用例表
    Testcase findTestcaseList(String testcaseListId, Long userId, Role userRole);

    //更新测试用例表信息，返回是否成功
    void updateTestcaseList(String testcaseListId, List<Testcase.TestcaseList> testcases, Long userId, Role userRole);

    //根据测试用例表id删除测试用例表，返回是否成功
    void removeTestcaseList(String testcaseListId, Long userId, Role userRole);
}
