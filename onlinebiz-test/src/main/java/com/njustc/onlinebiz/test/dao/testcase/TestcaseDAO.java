package com.njustc.onlinebiz.test.dao.testcase;

import com.njustc.onlinebiz.test.model.testcase.Testcase;
import com.njustc.onlinebiz.test.model.testcase.TestcaseStatus;

import java.util.List;

public interface TestcaseDAO {
    Testcase insertTestcaseList(Testcase testcaseList);

    Testcase findTestcaseListById(String testcaseListId);

    boolean updateContent(String testcaseListId, List<Testcase.TestcaseList> content);

    boolean updateStatus(String testcaseListId, TestcaseStatus status);

    boolean deleteTestcaseList(String testcaseListId);
}
