package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList.TestIssue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS011 {

    //所有软件测试问题
    private List<TestIssue> testIssues = new ArrayList<>();

    public JS011(TestIssueList testIssueList) {
        if (testIssueList == null) {
            throw new IllegalArgumentException("testIssueList is null");
        }
        this.testIssues = testIssueList.getTestIssues();
        if (this.testIssues == null) {
            this.testIssues = new ArrayList<>();
        }
    }
}

