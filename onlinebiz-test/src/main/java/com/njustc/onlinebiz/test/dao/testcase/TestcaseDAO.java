package com.njustc.onlinebiz.test.dao.testcase;

import com.njustc.onlinebiz.test.model.Testcase;
import com.njustc.onlinebiz.test.model.TestcaseStatus;

import java.util.List;

public interface TestcaseDAO {
    Testcase insertTestcaseList(Testcase testcaseList);

    Testcase findTestcaseListById(String testcaseListId);

    boolean updateContent(String testcaseListId, List<Testcase.TestcaseList> content);

    boolean updateStatus(String testcaseListId, TestcaseStatus status);

    boolean deleteTestcaseList(String testcaseListId);
}
