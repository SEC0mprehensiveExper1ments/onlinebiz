package com.njustc.onlinebiz.test.dao.testcase;

import com.njustc.onlinebiz.common.model.test.testcase.Testcase;

import java.util.List;

public interface TestcaseDAO {
    Testcase insertTestcaseList(Testcase testcaseList);

    Testcase findTestcaseListById(String testcaseListId);

    boolean updateContent(String testcaseListId, List<Testcase.TestcaseList> content);

    boolean deleteTestcaseList(String testcaseListId);
}
