package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList.TestIssue;
import com.njustc.onlinebiz.doc.model.JS011;
import com.njustc.onlinebiz.doc.service.DocServiceJS011;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JS011Test {

    @Autowired
    DocServiceJS011 docServiceJS011;

    @Test
    void testFillJS011Success() {
        List<TestIssue> testIssues = new ArrayList<>();
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testIssues.add(new TestIssue("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));

        JS011 newJson = new JS011().setTestIssues(testIssues);
        System.out.println(docServiceJS011.fill("1111", newJson));
    }
}
