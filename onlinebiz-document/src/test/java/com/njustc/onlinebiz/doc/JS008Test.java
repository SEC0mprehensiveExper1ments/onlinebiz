package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.test.testcase.Testcase.TestcaseList;
import com.njustc.onlinebiz.doc.model.JS008;
import com.njustc.onlinebiz.doc.service.DocServiceJS008;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JS008Test {

    @Autowired
    DocServiceJS008 docServiceJS008;

    @Test
    void testFillJS008Success() {
        List<TestcaseList> testcases = new ArrayList<>();
        testcases.add(new TestcaseList("你好1", "你好", "你好a", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好2", "你好", "你好r", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好3", "你好", "你好t", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好4", "你好", "你好y", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好5", "你好", "你好u", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好6", "你好", "你好i", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好7", "你好", "你好o", "你好", "你好", "你好", "你好"));
        testcases.add(new TestcaseList("你好8", "你好", "你好p", "你好", "你好", "你好", "你好"));

        JS008 newJson = new JS008().setTestcases(testcases);
        System.out.println(docServiceJS008.fill("1111", newJson));
    }
}
